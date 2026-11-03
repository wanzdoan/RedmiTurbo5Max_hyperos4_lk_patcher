// File offset entry: 0xa2814

/* WARNING: Removing unreachable block (ram,0x0209c5b0) */
/* WARNING: Removing unreachable block (ram,0x0209c5a0) */
/* WARNING: Removing unreachable block (ram,0x020a1dfc) */
/* WARNING: Removing unreachable block (ram,0x0209b5e0) */
/* WARNING: Removing unreachable block (ram,0x0209b5e8) */
/* WARNING: Removing unreachable block (ram,0x0209b668) */
/* WARNING: Removing unreachable block (ram,0x020a1e08) */

ulong FUN_020a2714(long param_1,long param_2,int param_3)

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
  
  uVar3 = 0x100006;
  if (((param_1 == 0) || (param_2 == 0)) ||
     (uVar3 = FUN_0209c58c(param_1,0x20,0x21224a0,0), (int)uVar3 != 0)) {
    return uVar3;
  }
  if (param_2 == 0) {
    return 0xffffffff;
  }
  if ((cRam0000000002122248 == '\x01') && (iVar1 = FUN_020a0710(), iVar1 == 5)) {
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
    uStack_bc = (undefined4)param_2;
    uStack_c8 = 4;
    uStack_c4 = 0x115;
    bStack_c0 = bStack_c0 & 0xf0 | 3;
    uStack_b4 = SUB84(&uStack_80,0);
    iStack_b8 = param_3;
    FUN_020a0df0(param_2,param_3);
    FUN_020a0df0(&uStack_80,0x40);
    FUN_020a07dc(auStack_158,&uStack_c8);
    FUN_020915ac(auStack_110,auStack_158,0x48);
    if (auStack_110[0] == 0) {
      FUN_0208ada0(0x20b1031,0x20b5155);
      FUN_020a0df0(&uStack_80,0x40);
      FUN_020a0dfc(0x21224c0,&uStack_80,0x20);
    }
    else {
      FUN_0208ada0(0x20b7047,0x20b5155);
    }
    if (*plRam0000000002103280 != lStack_38) {
      auVar4 = FUN_02091090((ulong)auStack_110[0]);
      uVar2 = auVar4._0_8_;
      uVar3 = uVar2 & 0xffffffffffffffc0;
      do {
        DC_CIVAC(uVar3);
        uVar3 = uVar3 + 0x40;
      } while (uVar3 < uVar2 + auVar4._8_8_);
      DataSynchronizationBarrier(3,3,0);
      return uVar2;
    }
    return (ulong)auStack_110[0];
  }
  if (param_2 == 0) {
    uVar3 = 2;
  }
  else if (param_3 == 0) {
    uVar3 = 1;
  }
  else {
    FUN_020abf40(0x10000000,1);
    FUN_020abea8(0x10000000,2);
    FUN_020abfa4(0x18000000,1);
    uVar3 = FUN_020aa860(0x18005000,param_2,param_3,0x21224c0);
    uVar3 = uVar3 & 0xffffffff;
    FUN_020abea8(0x10000000,0);
  }
  return uVar3;
}


