import defaultAvatar from '@/assets/default-avatar.png';

export function getAvatarUrl(avatar) {
  if (!avatar) return defaultAvatar;
  if (avatar.startsWith('http')) return avatar;
  return `http://localhost:9090${avatar}`;
}