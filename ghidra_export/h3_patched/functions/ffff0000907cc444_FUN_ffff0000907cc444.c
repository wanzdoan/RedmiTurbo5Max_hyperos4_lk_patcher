// ffff0000907cc444  size=268  FUN_ffff0000907cc444

undefined4 FUN_ffff0000907cc444(long *param_1,long param_2,ulong *param_3)

{
  uint uVar1;
  undefined4 uVar2;
  byte *pbVar3;
  ulong uVar4;
  byte *pbVar5;
  
  pbVar3 = (byte *)*param_1;
  uVar4 = param_2 - (long)pbVar3;
  if ((long)uVar4 < 1) {
    return 0xffffffa0;
  }
  if ((char)*pbVar3 < '\0') {
    switch(*pbVar3 & 0x7f) {
    case 1:
      if (uVar4 < 2) {
        return 0xffffffa0;
      }
      uVar4 = (ulong)pbVar3[1];
      pbVar5 = pbVar3 + 2;
      break;
    case 2:
      if (uVar4 < 3) {
        return 0xffffffa0;
      }
      pbVar5 = pbVar3 + 3;
      uVar4 = (ulong)((uint)(*(ushort *)(pbVar3 + 1) >> 8) |
                     (*(ushort *)(pbVar3 + 1) & 0xff00ff) << 8);
      break;
    case 3:
      if (uVar4 < 4) {
        return 0xffffffa0;
      }
      pbVar5 = pbVar3 + 4;
      uVar4 = (ulong)pbVar3[1] << 0x10 | (ulong)pbVar3[2] << 8 | (ulong)pbVar3[3];
      break;
    case 4:
      if (uVar4 < 5) {
        return 0xffffffa0;
      }
      pbVar5 = pbVar3 + 5;
      uVar1 = (*(uint *)(pbVar3 + 1) & 0xff00ff00) >> 8 | (*(uint *)(pbVar3 + 1) & 0xff00ff) << 8;
      uVar4 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
      break;
    default:
      return 0xffffff9c;
    }
    *param_3 = uVar4;
    *param_1 = (long)pbVar5;
  }
  else {
    pbVar5 = pbVar3 + 1;
    *param_1 = (long)pbVar5;
    uVar4 = (ulong)*pbVar3;
    *param_3 = uVar4;
  }
  uVar2 = 0xffffffa0;
  if (uVar4 <= (ulong)(param_2 - (long)pbVar5)) {
    uVar2 = 0;
  }
  return uVar2;
}


