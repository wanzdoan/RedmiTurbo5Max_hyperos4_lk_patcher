
void FUN_ffff0000be0341ec(uint param_1)

{
  int iVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  uint uVar5;
  ulong uVar6;
  
  if ((int)(uint)DAT_ffff0000be121810 <= (int)DAT_ffff0000be121800) {
    DAT_ffff0000be121800 = 0;
    DAT_ffff0000be121820 = DAT_ffff0000be121820 + 1;
    if ((int)DAT_ffff0000be121808 <= DAT_ffff0000be121820) {
      if (DAT_ffff0000be121818 == 0) {
        lVar4 = 0;
        lVar3 = DAT_ffff0000be121840;
      }
      else {
        uVar6 = (ulong)(uint)(*(int *)(DAT_ffff0000be121818 + 0x10) *
                              *(int *)(DAT_ffff0000be121818 + 0x14) * 0x10);
        lVar4 = (DAT_ffff0000be121808 + -1) * uVar6;
        lVar3 = DAT_ffff0000be121840 + uVar6;
      }
      FUN_ffff0000be0607b0(DAT_ffff0000be121840,lVar3,lVar4);
      if (DAT_ffff0000be121818 == 0) {
        uVar6 = 0;
      }
      else {
        uVar6 = (ulong)(uint)(*(int *)(DAT_ffff0000be121818 + 0x10) *
                              *(int *)(DAT_ffff0000be121818 + 0x14) * 0x10);
      }
      FUN_ffff0000be060ae0
                ((DAT_ffff0000be121840 - uVar6) +
                 CONCAT44(DAT_ffff0000be121808._4_4_,(int)DAT_ffff0000be121808) * uVar6,0);
      DAT_ffff0000be121820 = DAT_ffff0000be121820 + -1;
    }
  }
  switch(param_1 & 0xff) {
  case 8:
    if ((int)DAT_ffff0000be121800 < 1) {
      if (DAT_ffff0000be121820 < 2) {
        DAT_ffff0000be121820 = 1;
      }
      DAT_ffff0000be121820 = DAT_ffff0000be121820 + -1;
      DAT_ffff0000be121800 = (uint)DAT_ffff0000be121810;
    }
    DAT_ffff0000be121800 = DAT_ffff0000be121800 - 1;
    FUN_ffff0000be0346bc(DAT_ffff0000be121800 * 8,DAT_ffff0000be121820 << 4,0x20);
    return;
  case 9:
    DAT_ffff0000be121800 = DAT_ffff0000be121800 & 0xfffffff0 | 8;
    if ((int)(uint)DAT_ffff0000be121810 <= (int)DAT_ffff0000be121800) {
      DAT_ffff0000be121800 = 0;
      DAT_ffff0000be121820 = DAT_ffff0000be121820 + 1;
      if ((int)DAT_ffff0000be121808 <= DAT_ffff0000be121820) {
        if (DAT_ffff0000be121818 == 0) {
          lVar4 = 0;
          lVar3 = DAT_ffff0000be121840;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(DAT_ffff0000be121818 + 0x10) *
                                *(int *)(DAT_ffff0000be121818 + 0x14) * 0x10);
          lVar4 = (CONCAT44(DAT_ffff0000be121808._4_4_,(int)DAT_ffff0000be121808) + -1) * uVar6;
          lVar3 = DAT_ffff0000be121840 + uVar6;
        }
        FUN_ffff0000be0607b0(DAT_ffff0000be121840,lVar3,lVar4);
        if (DAT_ffff0000be121818 == 0) {
          uVar6 = 0;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(DAT_ffff0000be121818 + 0x10) *
                                *(int *)(DAT_ffff0000be121818 + 0x14) * 0x10);
        }
        FUN_ffff0000be060ae0
                  ((DAT_ffff0000be121840 - uVar6) +
                   CONCAT44(DAT_ffff0000be121808._4_4_,(int)DAT_ffff0000be121808) * uVar6,0);
        DAT_ffff0000be121820 = DAT_ffff0000be121820 + -1;
      }
    }
    break;
  case 10:
    if ((DAT_ffff0000be121800 != 0) || ((DAT_ffff0000be1217fc & 1) == 0)) {
      DAT_ffff0000be121800 = 0;
      DAT_ffff0000be121820 = DAT_ffff0000be121820 + 1;
      if ((int)DAT_ffff0000be121808 <= DAT_ffff0000be121820) {
        if (DAT_ffff0000be121818 == 0) {
          lVar4 = 0;
          lVar3 = DAT_ffff0000be121840;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(DAT_ffff0000be121818 + 0x10) *
                                *(int *)(DAT_ffff0000be121818 + 0x14) * 0x10);
          lVar4 = (CONCAT44(DAT_ffff0000be121808._4_4_,(int)DAT_ffff0000be121808) + -1) * uVar6;
          lVar3 = DAT_ffff0000be121840 + uVar6;
        }
        FUN_ffff0000be0607b0(DAT_ffff0000be121840,lVar3,lVar4);
        if (DAT_ffff0000be121818 == 0) {
          uVar6 = 0;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(DAT_ffff0000be121818 + 0x10) *
                                *(int *)(DAT_ffff0000be121818 + 0x14) * 0x10);
        }
        FUN_ffff0000be060ae0
                  ((DAT_ffff0000be121840 - uVar6) +
                   CONCAT44(DAT_ffff0000be121808._4_4_,(int)DAT_ffff0000be121808) * uVar6,0);
        DAT_ffff0000be121820 = DAT_ffff0000be121820 + -1;
      }
    }
    DAT_ffff0000be1217fc = 0;
    break;
  default:
    iVar1 = FUN_ffff0000be060f54(&DAT_ffff0000be0ae340,&DAT_ffff0000be0aa587,2);
    if (iVar1 == 0) {
      iVar2 = DAT_ffff0000be121800 << 3;
      iVar1 = ((int)DAT_ffff0000be121808 - DAT_ffff0000be121820) * 0x10;
    }
    else {
      iVar1 = FUN_ffff0000be060f54(&DAT_ffff0000be0ae340,&DAT_ffff0000be0be554,3);
      if (iVar1 == 0) {
        uVar5 = (uint)DAT_ffff0000be121810 + ~DAT_ffff0000be121800;
        iVar2 = (int)DAT_ffff0000be121808 - DAT_ffff0000be121820;
      }
      else {
        iVar1 = FUN_ffff0000be060f54(&DAT_ffff0000be0ae340,&DAT_ffff0000be0bc0e4,3);
        uVar5 = DAT_ffff0000be121800;
        iVar2 = DAT_ffff0000be121820;
        if (iVar1 == 0) {
          iVar1 = (DAT_ffff0000be121820 + ((uint)DAT_ffff0000be121810 - (int)DAT_ffff0000be121808))
                  * 0x10;
          iVar2 = ((int)DAT_ffff0000be121808 - DAT_ffff0000be121800) * 8 + -0x50;
          goto LAB_ffff0000be034584;
        }
      }
      iVar1 = uVar5 << 3;
      iVar2 = iVar2 << 4;
    }
LAB_ffff0000be034584:
    FUN_ffff0000be0346bc(iVar1,iVar2,param_1);
    DAT_ffff0000be121800 = DAT_ffff0000be121800 + 1;
    if ((int)(uint)DAT_ffff0000be121810 <= (int)DAT_ffff0000be121800) {
      DAT_ffff0000be121800 = 0;
      DAT_ffff0000be121820 = DAT_ffff0000be121820 + 1;
      if ((int)DAT_ffff0000be121808 <= DAT_ffff0000be121820) {
        if (DAT_ffff0000be121818 == 0) {
          lVar4 = 0;
          lVar3 = DAT_ffff0000be121840;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(DAT_ffff0000be121818 + 0x10) *
                                *(int *)(DAT_ffff0000be121818 + 0x14) * 0x10);
          lVar4 = (CONCAT44(DAT_ffff0000be121808._4_4_,(int)DAT_ffff0000be121808) + -1) * uVar6;
          lVar3 = DAT_ffff0000be121840 + uVar6;
        }
        FUN_ffff0000be0607b0(DAT_ffff0000be121840,lVar3,lVar4);
        if (DAT_ffff0000be121818 == 0) {
          uVar6 = 0;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(DAT_ffff0000be121818 + 0x10) *
                                *(int *)(DAT_ffff0000be121818 + 0x14) * 0x10);
        }
        FUN_ffff0000be060ae0
                  ((DAT_ffff0000be121840 - uVar6) +
                   CONCAT44(DAT_ffff0000be121808._4_4_,(int)DAT_ffff0000be121808) * uVar6,0);
        DAT_ffff0000be121820 = DAT_ffff0000be121820 + -1;
      }
      DAT_ffff0000be1217fc = 1;
    }
    break;
  case 0xd:
    DAT_ffff0000be121800 = 0;
  }
  return;
}


