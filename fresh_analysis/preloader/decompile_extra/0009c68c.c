// File offset entry: 0x9c68c

ulong FUN_0209c58c(long param_1,int param_2,long param_3,int param_4)

{
  int iVar1;
  ulong uVar2;
  ulong uVar3;
  undefined1 auVar4 [16];
  undefined1 auStack_158 [72];
  uint auStack_110 [18];
  undefined4 uStack_c8;
  undefined2 uStack_c4;
  byte bStack_c0;
  undefined4 uStack_bc;
  int iStack_b8;
  undefined4 uStack_b4;
  undefined8 uStack_80;
  undefined8 uStack_78;
  undefined8 uStack_70;
  undefined8 uStack_68;
  undefined8 uStack_60;
  undefined8 uStack_58;
  undefined8 uStack_50;
  undefined8 uStack_48;
  long lStack_38;
  
  if (param_4 - 1U < 2) {
    if ((param_1 != 0) && (param_3 != 0)) {
      if (param_2 != 0) {
        FUN_020abf40(0x10000000,1);
        FUN_020abea8(0x10000000,2);
        FUN_020abfa4(0x18000000,1);
        uVar2 = FUN_020aaae8(0x18005000,param_1,param_2,param_3);
        FUN_020abea8(0x10000000,0);
        return uVar2 & 0xffffffff;
      }
      return 1;
    }
    return 0xffffffff;
  }
  if (param_4 != 0) {
    return 0x10101b;
  }
  if ((param_1 != 0) && (param_3 != 0)) {
    if ((cRam0000000002122248 != '\x01') || (iVar1 = FUN_020a0710(), iVar1 != 5)) {
      if (param_1 == 0) {
        uVar2 = 2;
      }
      else if (param_2 == 0) {
        uVar2 = 1;
      }
      else {
        FUN_020abf40(0x10000000,1);
        FUN_020abea8(0x10000000,2);
        FUN_020abfa4(0x18000000,1);
        uVar2 = FUN_020aa860(0x18005000,param_1,param_2,param_3);
        uVar2 = uVar2 & 0xffffffff;
        FUN_020abea8(0x10000000,0);
      }
      return uVar2;
    }
    lStack_38 = *plRam0000000002103280;
    uStack_50 = 0;
    uStack_48 = 0;
    uStack_60 = 0;
    uStack_58 = 0;
    uStack_70 = 0;
    uStack_68 = 0;
    uStack_80 = 0;
    uStack_78 = 0;
    FUN_02091664(&uStack_c8,0,0x48);
    FUN_02091664(auStack_110,0,0x48);
    FUN_020a0aa0();
    uStack_bc = (undefined4)param_1;
    uStack_c8 = 4;
    uStack_c4 = 0x115;
    bStack_c0 = bStack_c0 & 0xf0 | 3;
    uStack_b4 = SUB84(&uStack_80,0);
    iStack_b8 = param_2;
    FUN_020a0df0(param_1,param_2);
    FUN_020a0df0(&uStack_80,0x40);
    FUN_020a07dc(auStack_158,&uStack_c8);
    FUN_020915ac(auStack_110,auStack_158,0x48);
    if (auStack_110[0] == 0) {
      FUN_0208ada0(0x20b1031,0x20b5155);
      FUN_020a0df0(&uStack_80,0x40);
      FUN_020a0dfc(param_3,&uStack_80,0x20);
    }
    else {
      FUN_0208ada0(0x20b7047,0x20b5155);
    }
    if (*plRam0000000002103280 == lStack_38) {
      return (ulong)auStack_110[0];
    }
    auVar4 = FUN_02091090((ulong)auStack_110[0]);
    uVar3 = auVar4._0_8_;
    uVar2 = uVar3 & 0xffffffffffffffc0;
    do {
      DC_CIVAC(uVar2);
      uVar2 = uVar2 + 0x40;
    } while (uVar2 < uVar3 + auVar4._8_8_);
    DataSynchronizationBarrier(3,3,0);
    return uVar3;
  }
  return 0xffffffff;
}


