
void FUN_ffff0000be01c070(undefined8 param_1,undefined8 param_2,ulong param_3)

{
  int iVar1;
  uint uVar2;
  ulong uVar3;
  long lVar4;
  uint uVar5;
  
  if ((param_3 != 0) && ((DAT_ffff0000be10ccb0 & 1) == 0)) {
    if ((DAT_ffff0000be10ccb8 == 0) && (DAT_ffff0000be10ccc0 != -1)) {
      if (DAT_ffff0000be10ccc0 != 0x5000) {
        lVar4 = (long)DAT_ffff0000be10ccc0;
        uVar3 = 0x5000 - lVar4;
        if (lVar4 + param_3 < 0x5001) {
          uVar3 = param_3;
        }
        FUN_ffff0000be0607b0(&DAT_ffff0000be10ccc4 + lVar4,param_2,uVar3);
        DAT_ffff0000be10ccc0 = DAT_ffff0000be10ccc0 + (int)uVar3;
      }
    }
    else if ((((*(uint *)(DAT_ffff0000be10cca8 + 0x10) >> 9 & 1) != 0) &&
             ((*(uint *)(DAT_ffff0000be10cca8 + 0x10) >> 4 & 1) == 0)) &&
            (DAT_ffff0000be10ccb8 != 0)) {
      if (DAT_ffff0000be10ccc0 < 1) {
        uVar5 = *(uint *)(DAT_ffff0000be10cca8 + 0x58);
        lVar4 = DAT_ffff0000be10cca8;
      }
      else {
        iVar1 = *(uint *)(DAT_ffff0000be10cca8 + 0x58) + *(uint *)(DAT_ffff0000be10cca8 + 0x54);
        if (*(uint *)(DAT_ffff0000be10cca8 + 0x1c) < (uint)(iVar1 + DAT_ffff0000be10ccc0)) {
          DAT_ffff0000be10ccc0 = *(uint *)(DAT_ffff0000be10cca8 + 0x1c) - iVar1;
        }
        FUN_ffff0000be0607b0
                  (DAT_ffff0000be10ccb8 + (ulong)*(uint *)(DAT_ffff0000be10cca8 + 0x54) +
                   (ulong)*(uint *)(DAT_ffff0000be10cca8 + 0x58),&DAT_ffff0000be10ccc4,
                   (long)DAT_ffff0000be10ccc0);
        lVar4 = DAT_ffff0000be10cca8;
        uVar5 = *(int *)(DAT_ffff0000be10cca8 + 0x58) + DAT_ffff0000be10ccc0;
        *(uint *)(DAT_ffff0000be10cca8 + 0x58) = uVar5;
        DAT_ffff0000be10ccc0 = -1;
      }
      uVar2 = uVar5 + *(uint *)(lVar4 + 0x54);
      if ((ulong)*(uint *)(lVar4 + 0x1c) < uVar2 + param_3) {
        param_3 = (ulong)(*(uint *)(lVar4 + 0x1c) - uVar2);
        *(uint *)(lVar4 + 0x10) = *(uint *)(lVar4 + 0x10) | 0x10;
      }
      FUN_ffff0000be0607b0
                (DAT_ffff0000be10ccb8 + (ulong)*(uint *)(lVar4 + 0x54) + (ulong)uVar5,param_2,
                 param_3);
      *(int *)(DAT_ffff0000be10cca8 + 0x58) = *(int *)(DAT_ffff0000be10cca8 + 0x58) + (int)param_3;
    }
  }
  return;
}


