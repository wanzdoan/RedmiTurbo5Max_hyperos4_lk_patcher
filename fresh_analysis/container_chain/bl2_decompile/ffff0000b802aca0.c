
void FUN_ffff0000b802aca0(uint param_1)

{
  int iVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  uint uVar5;
  ulong uVar6;
  
  if ((int)DAT_ffff0000b8125018 <= (int)DAT_ffff0000b8125008) {
    DAT_ffff0000b8125008 = 0;
    DAT_ffff0000b8125028 = DAT_ffff0000b8125028 + 1;
    if (DAT_ffff0000b8125010 <= DAT_ffff0000b8125028) {
      if (DAT_ffff0000b8125020 == 0) {
        lVar4 = 0;
        lVar3 = DAT_ffff0000b8125048;
      }
      else {
        uVar6 = (ulong)(uint)(*(int *)(DAT_ffff0000b8125020 + 0x10) *
                              *(int *)(DAT_ffff0000b8125020 + 0x14) * 0x10);
        lVar4 = (CONCAT44(uRamffff0000b8125014,DAT_ffff0000b8125010) + -1) * uVar6;
        lVar3 = DAT_ffff0000b8125048 + uVar6;
      }
      FUN_ffff0000b804cd40(DAT_ffff0000b8125048,lVar3,lVar4);
      if (DAT_ffff0000b8125020 == 0) {
        uVar6 = 0;
      }
      else {
        uVar6 = (ulong)(uint)(*(int *)(DAT_ffff0000b8125020 + 0x10) *
                              *(int *)(DAT_ffff0000b8125020 + 0x14) * 0x10);
      }
      FUN_ffff0000b804d070
                ((DAT_ffff0000b8125048 - uVar6) +
                 CONCAT44(uRamffff0000b8125014,DAT_ffff0000b8125010) * uVar6,0);
      DAT_ffff0000b8125028 = DAT_ffff0000b8125028 + -1;
    }
  }
  switch(param_1 & 0xff) {
  case 8:
    if ((int)DAT_ffff0000b8125008 < 1) {
      if (DAT_ffff0000b8125028 < 2) {
        DAT_ffff0000b8125028 = 1;
      }
      DAT_ffff0000b8125028 = DAT_ffff0000b8125028 + -1;
      DAT_ffff0000b8125008 = DAT_ffff0000b8125018;
    }
    DAT_ffff0000b8125008 = DAT_ffff0000b8125008 - 1;
    FUN_ffff0000b802b170(DAT_ffff0000b8125008 * 8,DAT_ffff0000b8125028 << 4,0x20);
    return;
  case 9:
    DAT_ffff0000b8125008 = DAT_ffff0000b8125008 & 0xfffffff0 | 8;
    if ((int)DAT_ffff0000b8125018 <= (int)DAT_ffff0000b8125008) {
      DAT_ffff0000b8125008 = 0;
      DAT_ffff0000b8125028 = DAT_ffff0000b8125028 + 1;
      if (DAT_ffff0000b8125010 <= DAT_ffff0000b8125028) {
        if (DAT_ffff0000b8125020 == 0) {
          lVar4 = 0;
          lVar3 = DAT_ffff0000b8125048;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(DAT_ffff0000b8125020 + 0x10) *
                                *(int *)(DAT_ffff0000b8125020 + 0x14) * 0x10);
          lVar4 = (CONCAT44(uRamffff0000b8125014,DAT_ffff0000b8125010) + -1) * uVar6;
          lVar3 = DAT_ffff0000b8125048 + uVar6;
        }
        FUN_ffff0000b804cd40(DAT_ffff0000b8125048,lVar3,lVar4);
        if (DAT_ffff0000b8125020 == 0) {
          uVar6 = 0;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(DAT_ffff0000b8125020 + 0x10) *
                                *(int *)(DAT_ffff0000b8125020 + 0x14) * 0x10);
        }
        FUN_ffff0000b804d070
                  ((DAT_ffff0000b8125048 - uVar6) +
                   CONCAT44(uRamffff0000b8125014,DAT_ffff0000b8125010) * uVar6,0);
        DAT_ffff0000b8125028 = DAT_ffff0000b8125028 + -1;
      }
    }
    break;
  case 10:
    if ((DAT_ffff0000b8125008 != 0) || ((DAT_ffff0000b8125004 & 1) == 0)) {
      DAT_ffff0000b8125008 = 0;
      DAT_ffff0000b8125028 = DAT_ffff0000b8125028 + 1;
      if (DAT_ffff0000b8125010 <= DAT_ffff0000b8125028) {
        if (DAT_ffff0000b8125020 == 0) {
          lVar4 = 0;
          lVar3 = DAT_ffff0000b8125048;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(DAT_ffff0000b8125020 + 0x10) *
                                *(int *)(DAT_ffff0000b8125020 + 0x14) * 0x10);
          lVar4 = (CONCAT44(uRamffff0000b8125014,DAT_ffff0000b8125010) + -1) * uVar6;
          lVar3 = DAT_ffff0000b8125048 + uVar6;
        }
        FUN_ffff0000b804cd40(DAT_ffff0000b8125048,lVar3,lVar4);
        if (DAT_ffff0000b8125020 == 0) {
          uVar6 = 0;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(DAT_ffff0000b8125020 + 0x10) *
                                *(int *)(DAT_ffff0000b8125020 + 0x14) * 0x10);
        }
        FUN_ffff0000b804d070
                  ((DAT_ffff0000b8125048 - uVar6) +
                   CONCAT44(uRamffff0000b8125014,DAT_ffff0000b8125010) * uVar6,0);
        DAT_ffff0000b8125028 = DAT_ffff0000b8125028 + -1;
      }
    }
    DAT_ffff0000b8125004 = 0;
    break;
  default:
    iVar1 = FUN_ffff0000b804d4c4(&DAT_ffff0000b808dbfe,&DAT_ffff0000b808ab6e,2);
    if (iVar1 == 0) {
      iVar2 = DAT_ffff0000b8125008 << 3;
      iVar1 = (DAT_ffff0000b8125010 - DAT_ffff0000b8125028) * 0x10;
    }
    else {
      iVar1 = FUN_ffff0000b804d4c4(&DAT_ffff0000b808dbfe,&DAT_ffff0000b809aad9,3);
      if (iVar1 == 0) {
        uVar5 = DAT_ffff0000b8125018 + ~DAT_ffff0000b8125008;
        iVar2 = DAT_ffff0000b8125010 - DAT_ffff0000b8125028;
      }
      else {
        iVar1 = FUN_ffff0000b804d4c4(&DAT_ffff0000b808dbfe,&DAT_ffff0000b8098f2a,3);
        uVar5 = DAT_ffff0000b8125008;
        iVar2 = DAT_ffff0000b8125028;
        if (iVar1 == 0) {
          iVar1 = (DAT_ffff0000b8125028 + (DAT_ffff0000b8125018 - DAT_ffff0000b8125010)) * 0x10;
          iVar2 = (DAT_ffff0000b8125010 - DAT_ffff0000b8125008) * 8 + -0x50;
          goto LAB_ffff0000b802b038;
        }
      }
      iVar1 = uVar5 << 3;
      iVar2 = iVar2 << 4;
    }
LAB_ffff0000b802b038:
    FUN_ffff0000b802b170(iVar1,iVar2,param_1);
    DAT_ffff0000b8125008 = DAT_ffff0000b8125008 + 1;
    if ((int)DAT_ffff0000b8125018 <= (int)DAT_ffff0000b8125008) {
      DAT_ffff0000b8125008 = 0;
      DAT_ffff0000b8125028 = DAT_ffff0000b8125028 + 1;
      if (DAT_ffff0000b8125010 <= DAT_ffff0000b8125028) {
        if (DAT_ffff0000b8125020 == 0) {
          lVar4 = 0;
          lVar3 = DAT_ffff0000b8125048;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(DAT_ffff0000b8125020 + 0x10) *
                                *(int *)(DAT_ffff0000b8125020 + 0x14) * 0x10);
          lVar4 = (CONCAT44(uRamffff0000b8125014,DAT_ffff0000b8125010) + -1) * uVar6;
          lVar3 = DAT_ffff0000b8125048 + uVar6;
        }
        FUN_ffff0000b804cd40(DAT_ffff0000b8125048,lVar3,lVar4);
        if (DAT_ffff0000b8125020 == 0) {
          uVar6 = 0;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(DAT_ffff0000b8125020 + 0x10) *
                                *(int *)(DAT_ffff0000b8125020 + 0x14) * 0x10);
        }
        FUN_ffff0000b804d070
                  ((DAT_ffff0000b8125048 - uVar6) +
                   CONCAT44(uRamffff0000b8125014,DAT_ffff0000b8125010) * uVar6,0);
        DAT_ffff0000b8125028 = DAT_ffff0000b8125028 + -1;
      }
      DAT_ffff0000b8125004 = 1;
    }
    break;
  case 0xd:
    DAT_ffff0000b8125008 = 0;
  }
  return;
}


