// File offset entry: 0x9c6c0

ulong FUN_0209c5c0(undefined8 param_1,undefined8 param_2,undefined8 param_3,int param_4)

{
  int iVar1;
  ulong uVar2;
  long lVar3;
  long lVar4;
  long lVar5;
  ulong uVar6;
  long lVar7;
  undefined1 auVar8 [16];
  undefined1 auStack_4d8 [72];
  uint auStack_490 [18];
  undefined4 uStack_448;
  undefined2 uStack_444;
  byte bStack_440;
  undefined4 uStack_43c;
  undefined4 uStack_438;
  undefined4 uStack_434;
  undefined8 uStack_400;
  undefined8 uStack_3f8;
  undefined8 uStack_3f0;
  undefined8 uStack_3e8;
  undefined8 uStack_3e0;
  undefined8 uStack_3d8;
  undefined8 uStack_3d0;
  undefined8 uStack_3c8;
  long lStack_3b8;
  undefined1 *puStack_3b0;
  undefined8 uStack_3a8;
  undefined1 auStack_358 [72];
  uint auStack_310 [18];
  undefined4 uStack_2c8;
  undefined2 uStack_2c4;
  byte bStack_2c0;
  undefined4 uStack_2bc;
  undefined4 uStack_2b8;
  undefined4 uStack_2b4;
  undefined4 uStack_2b0;
  undefined4 uStack_2ac;
  undefined1 auStack_280 [560];
  
  if (param_4 == 2) {
    lVar7 = func_0x0209ac10(0x200);
    if (lVar7 == 0) {
      uVar6 = 0xffffffff;
    }
    else {
      lVar3 = func_0x0209ac10(0x400);
      if (lVar3 == 0) {
        func_0x0209ad5c(lVar7,0x200);
        uVar6 = 0xfffffffe;
      }
      else {
        lVar4 = func_0x0209ac10(0x400);
        if (lVar4 == 0) {
          func_0x0209ad5c(lVar7,0x200);
          func_0x0209ad5c(lVar3,0x400);
          uVar6 = 0xfffffffd;
        }
        else {
          lVar5 = func_0x0209ac10(0x400);
          if (lVar5 == 0) {
            func_0x0209ad5c(lVar7,0x200);
            func_0x0209ad5c(lVar3,0x400);
            func_0x0209ad5c(lVar4,0x400);
            uVar6 = 0xfffffffc;
          }
          else {
            func_0x020a1238(lVar7,param_1,param_1,param_2,lVar3,lVar4,lVar5);
            iVar1 = 0xf;
            do {
              func_0x020a1238(lVar7,lVar7,lVar7,param_2,lVar3,lVar4,lVar5);
              iVar1 = iVar1 + -1;
            } while (iVar1 != 0);
            func_0x020a1238(lVar7,lVar7,param_1,param_2,lVar3,lVar4,lVar5);
            FUN_020915ac(param_3,lVar7,0x200);
            func_0x0209ad5c(lVar7,0x200);
            func_0x0209ad5c(lVar3,0x400);
            func_0x0209ad5c(lVar4,0x400);
            func_0x0209ad5c(lVar5,0x400);
            uVar6 = 0;
          }
        }
      }
    }
    return uVar6;
  }
  if (param_4 == 1) {
    FUN_02091664(0x2100ca8,0,0x180);
    uRam0000000002100ca8 = 1;
    uRam0000000002100caa = 1;
    FUN_020a0fac(0x2100ca8,0x180);
    FUN_020a0fac(param_1,0x180);
    FUN_020a0fac(param_2,0x180);
    uVar6 = func_0x0209bab8(param_1,0x2100ca8,param_2,param_3);
    FUN_020a0fac(param_3,0x180);
    return uVar6 & 0xffffffff;
  }
  if (param_4 != 0) {
    return 0x10101b;
  }
  iVar1 = FUN_020a0710();
  if (iVar1 == 5) {
    lVar7 = *plRam0000000002103280;
    FUN_02091664(auStack_280,0,0x200);
    FUN_02091664(&uStack_2c8,0,0x48);
    FUN_02091664(auStack_310,0,0x48);
    FUN_020a0aa0();
    uStack_2c8 = 4;
    uStack_2c4 = 0x114;
    uStack_2bc = (undefined4)param_1;
    uStack_2b8 = 0x100;
    bStack_2c0 = bStack_2c0 & 0xf0 | 5;
    uStack_2b4 = (undefined4)param_2;
    uStack_2b0 = 0x100;
    uStack_2ac = SUB84(auStack_280,0);
    FUN_020a0df0(param_1,0x100);
    FUN_020a0df0(param_2,0x100);
    FUN_020a0df0(auStack_280,0x200);
    FUN_020a07dc(auStack_358,&uStack_2c8);
    FUN_020915ac(auStack_310,auStack_358,0x48);
    uVar6 = (ulong)auStack_310[0];
    if (auStack_310[0] == 0) {
      FUN_0208ada0(0x20b20fd,0x20b5155);
      FUN_020a0df0(auStack_280,0x200);
      uVar6 = 0x100;
      FUN_020a0dfc(param_3,auStack_280,0x100);
    }
    else {
      FUN_0208ada0(0x20bd273,0x20b5155);
    }
    if (*plRam0000000002103280 == lVar7) {
      return (ulong)auStack_310[0];
    }
    auVar8 = FUN_02091090((ulong)auStack_310[0]);
    uStack_3a8 = 0x20a0c9c;
    lStack_3b8 = *plRam0000000002103280;
    uStack_3d0 = 0;
    uStack_3c8 = 0;
    uStack_3e0 = 0;
    uStack_3d8 = 0;
    uStack_3f0 = 0;
    uStack_3e8 = 0;
    uStack_400 = 0;
    uStack_3f8 = 0;
    puStack_3b0 = &stack0xffffffffffffffc0;
    FUN_02091664(&uStack_448,0,0x48);
    FUN_02091664(auStack_490,0,0x48);
    FUN_020a0aa0();
    uStack_43c = auVar8._0_4_;
    uStack_438 = auVar8._8_4_;
    uStack_448 = 4;
    uStack_444 = 0x115;
    bStack_440 = bStack_440 & 0xf0 | 3;
    uStack_434 = SUB84(&uStack_400,0);
    FUN_020a0df0(auVar8._0_8_,auVar8._8_8_ & 0xffffffff);
    FUN_020a0df0(&uStack_400,0x40);
    FUN_020a07dc(auStack_4d8,&uStack_448);
    FUN_020915ac(auStack_490,auStack_4d8,0x48);
    if (auStack_490[0] == 0) {
      FUN_0208ada0(0x20b1031,0x20b5155);
      FUN_020a0df0(&uStack_400,0x40);
      FUN_020a0dfc(uVar6,&uStack_400,0x20);
    }
    else {
      FUN_0208ada0(0x20b7047,0x20b5155);
    }
    if (*plRam0000000002103280 == lStack_3b8) {
      return (ulong)auStack_490[0];
    }
    auVar8 = FUN_02091090((ulong)auStack_490[0]);
    uVar2 = auVar8._0_8_;
    uVar6 = uVar2 & 0xffffffffffffffc0;
    do {
      DC_CIVAC(uVar6);
      uVar6 = uVar6 + 0x40;
    } while (uVar6 < uVar2 + auVar8._8_8_);
    DataSynchronizationBarrier(3,3,0);
    return uVar2;
  }
  FUN_02091664(0x2122128,0,0x100);
  uRam0000000002122128 = 1;
  uRam000000000212212a = 1;
  FUN_020a0fac(0x2122128,0x100);
  FUN_020a0fac(param_1,0x100);
  FUN_020a0fac(param_2,0x100);
  uVar6 = func_0x0209ba28(param_1,0x2122128,param_2,param_3);
  FUN_020a0fac(param_3,0x100);
  return uVar6 & 0xffffffff;
}


