// ffff0000907aee00  size=136  FUN_ffff0000907aee00

/* WARNING: Removing unreachable block (ram,0xffff0000907aeef8) */
/* WARNING: Removing unreachable block (ram,0xffff0000907aeed0) */

void FUN_ffff0000907aee00(int param_1,ulong param_2,long *param_3)

{
  ulong uVar1;
  byte bVar2;
  undefined1 uVar3;
  undefined1 uVar4;
  bool bVar6;
  long lVar7;
  ulong uVar8;
  undefined1 uVar5;
  
  if (param_2 < 0x18) {
    FUN_ffff0000907aee88(1);
    return;
  }
  if (param_2 < 0x100) {
    FUN_ffff0000907aee88(2);
    return;
  }
  if (param_2 < 0x10000) {
    FUN_ffff0000907aee88(3);
    return;
  }
  bVar6 = (int)(param_2 >> 0x20) != 0;
  uVar1 = 5;
  if (bVar6) {
    uVar1 = 9;
  }
  uVar8 = param_3[2];
  if (CARRY8(uVar8,uVar1)) {
    lVar7 = -1;
  }
  else {
    if ((uVar8 <= (ulong)param_3[1]) && (uVar1 <= param_3[1] - uVar8)) {
      bVar2 = (byte)param_2;
      if (param_2 < 0x18) {
        *(byte *)(*param_3 + uVar8) = bVar2 | (byte)(param_1 << 5);
      }
      else {
        uVar3 = (undefined1)(param_2 >> 8);
        uVar4 = (undefined1)(param_2 >> 0x10);
        uVar5 = (undefined1)(param_2 >> 0x18);
        if (bVar6) {
          *(byte *)(*param_3 + uVar8) = (byte)(param_1 << 5) | 0x1b;
          *(char *)(param_3[2] + *param_3 + 1) = (char)(param_2 >> 0x38);
          *(char *)(param_3[2] + *param_3 + 2) = (char)(param_2 >> 0x30);
          *(char *)(param_3[2] + *param_3 + 3) = (char)(param_2 >> 0x28);
          *(char *)(param_3[2] + *param_3 + 4) = (char)(param_2 >> 0x20);
          *(undefined1 *)(param_3[2] + *param_3 + 5) = uVar5;
          *(undefined1 *)(param_3[2] + *param_3 + 6) = uVar4;
          *(undefined1 *)(param_3[2] + *param_3 + 7) = uVar3;
          *(byte *)(param_3[2] + *param_3 + 8) = bVar2;
        }
        else {
          *(byte *)(*param_3 + uVar8) = (byte)(param_1 << 5) | 0x1a;
          *(undefined1 *)(param_3[2] + *param_3 + 1) = uVar5;
          *(undefined1 *)(param_3[2] + *param_3 + 2) = uVar4;
          *(undefined1 *)(param_3[2] + *param_3 + 3) = uVar3;
          *(byte *)(param_3[2] + *param_3 + 4) = bVar2;
        }
      }
    }
    lVar7 = param_3[2] + uVar1;
  }
  param_3[2] = lVar7;
  return;
}


