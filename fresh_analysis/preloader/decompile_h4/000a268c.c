// File offset entry: 0xa268c

/* WARNING: Removing unreachable block (ram,0x0209c5b0) */
/* WARNING: Removing unreachable block (ram,0x0209c5a0) */
/* WARNING: Removing unreachable block (ram,0x020a1dfc) */
/* WARNING: Removing unreachable block (ram,0x0209b5e0) */
/* WARNING: Removing unreachable block (ram,0x0209b5e8) */
/* WARNING: Removing unreachable block (ram,0x0209b668) */
/* WARNING: Removing unreachable block (ram,0x020a1e08) */

ulong FUN_020a258c(undefined8 param_1,undefined4 param_2,undefined8 param_3,int param_4,long param_5
                  )

{
  long lVar1;
  undefined1 *puVar2;
  undefined8 uVar3;
  int iVar4;
  undefined4 uVar5;
  ulong uVar6;
  undefined8 *puVar7;
  long lVar8;
  ulong uVar9;
  undefined1 auVar10 [16];
  undefined1 auStack_218 [72];
  uint auStack_1d0 [18];
  undefined4 uStack_188;
  undefined2 uStack_184;
  byte bStack_180;
  undefined4 uStack_17c;
  undefined4 uStack_178;
  undefined4 uStack_174;
  undefined8 uStack_140;
  undefined8 uStack_138;
  undefined8 uStack_130;
  undefined8 uStack_128;
  undefined8 uStack_120;
  undefined8 uStack_118;
  undefined8 uStack_110;
  undefined8 uStack_108;
  long lStack_f8;
  undefined1 *puStack_d0;
  undefined8 uStack_c8;
  undefined8 uStack_98;
  undefined8 uStack_90;
  undefined8 uStack_88;
  undefined8 uStack_80;
  undefined8 uStack_78;
  undefined8 uStack_70;
  undefined8 uStack_68;
  undefined8 uStack_60;
  undefined8 uStack_58;
  ulong uStack_50;
  long lStack_48;
  
  lStack_48 = *plRam0000000002103280;
  uStack_58 = 0;
  uStack_50 = 0;
  uVar3 = 0x20b7fe6;
  if (param_4 != 0) {
    uVar3 = 0x20b706a;
  }
  uStack_68 = 0;
  uStack_60 = 0;
  uStack_78 = 0;
  uStack_70 = 0;
  uStack_88 = 0;
  uStack_80 = 0;
  uStack_98 = 0;
  uStack_90 = 0;
  func_0x020a0e9c(&uStack_58,uVar3,0x10);
  uStack_50 = uStack_50 & 0xffffffffffffff;
  uVar5 = func_0x020a0e20(&uStack_58);
  puVar7 = &uStack_98;
  uVar6 = FUN_0209c58c(param_1,param_2,puVar7,uRam00000000021224e0);
  if ((int)uVar6 == 0) {
    puVar7 = &uStack_58;
    uVar6 = func_0x0209f8b0(&uStack_98,uRam0000000002122b80,puVar7,uVar5,param_3,0x20,param_5,0x20);
    if ((int)uVar6 == 0) {
      lVar8 = 0;
      if (param_4 == 0) {
        do {
          puVar2 = (undefined1 *)(param_5 + 1 + lVar8);
          *(uint *)(lVar8 + 0x1800d700) =
               CONCAT13(puVar2[2],CONCAT12(puVar2[1],CONCAT11(*puVar2,puVar2[-1])));
          lVar8 = lVar8 + 4;
        } while (lVar8 != 0x20);
      }
      else {
        do {
          puVar2 = (undefined1 *)(param_5 + 1 + lVar8);
          lVar1 = lVar8 + 4;
          *(uint *)(lVar8 + 0x1800d720) =
               CONCAT13(puVar2[2],CONCAT12(puVar2[1],CONCAT11(*puVar2,puVar2[-1])));
          lVar8 = lVar1;
        } while (lVar1 != 0x20);
      }
      uVar6 = 0;
    }
  }
  if (*plRam0000000002103280 == lStack_48) {
    return uVar6;
  }
  auVar10 = FUN_02091090(uVar6);
  lVar8 = auVar10._8_8_;
  uVar6 = 0x100006;
  if ((auVar10._0_8_ != 0) && (lVar8 != 0)) {
    uVar9 = (ulong)puVar7 & 0xffffffff;
    uVar6 = FUN_0209c58c(auVar10._0_8_,0x20,0x21224a0,0);
    if ((int)uVar6 == 0) {
      if (lVar8 == 0) {
        return 0xffffffff;
      }
      uStack_c8 = 0x20a2714;
      puStack_d0 = &stack0xffffffffffffffc0;
      uVar3 = 0x20a2714;
      if ((cRam0000000002122248 == '\x01') &&
         (iVar4 = FUN_020a0710(), uVar3 = uStack_c8, iVar4 == 5)) {
        lStack_f8 = *plRam0000000002103280;
        uStack_110 = 0;
        uStack_108 = 0;
        uStack_120 = 0;
        uStack_118 = 0;
        uStack_130 = 0;
        uStack_128 = 0;
        uStack_140 = 0;
        uStack_138 = 0;
        FUN_02091664(&uStack_188,0,0x48);
        FUN_02091664(auStack_1d0,0,0x48);
        FUN_020a0aa0();
        uStack_17c = auVar10._8_4_;
        uStack_188 = 4;
        uStack_184 = 0x115;
        bStack_180 = bStack_180 & 0xf0 | 3;
        uStack_174 = SUB84(&uStack_140,0);
        uStack_178 = (int)puVar7;
        FUN_020a0df0(lVar8,uVar9);
        FUN_020a0df0(&uStack_140,0x40);
        FUN_020a07dc(auStack_218,&uStack_188);
        FUN_020915ac(auStack_1d0,auStack_218,0x48);
        if (auStack_1d0[0] == 0) {
          FUN_0208ada0(0x20b1031,0x20b5155);
          FUN_020a0df0(&uStack_140,0x40);
          FUN_020a0dfc(0x21224c0,&uStack_140,0x20);
        }
        else {
          FUN_0208ada0(0x20b7047,0x20b5155);
        }
        if (*plRam0000000002103280 == lStack_f8) {
          return (ulong)auStack_1d0[0];
        }
        auVar10 = FUN_02091090((ulong)auStack_1d0[0]);
        uVar9 = auVar10._0_8_;
        uVar6 = uVar9 & 0xffffffffffffffc0;
        do {
          DC_CIVAC(uVar6);
          uVar6 = uVar6 + 0x40;
        } while (uVar6 < uVar9 + auVar10._8_8_);
        DataSynchronizationBarrier(3,3,0);
        return uVar9;
      }
      uStack_c8 = uVar3;
      if (lVar8 == 0) {
        uVar6 = 2;
      }
      else if (uVar9 == 0) {
        uVar6 = 1;
      }
      else {
        FUN_020abf40(0x10000000,1);
        FUN_020abea8(0x10000000,2);
        FUN_020abfa4(0x18000000,1);
        uVar6 = FUN_020aa860(0x18005000,lVar8,uVar9,0x21224c0);
        uVar6 = uVar6 & 0xffffffff;
        FUN_020abea8(0x10000000,0);
      }
      return uVar6;
    }
  }
  return uVar6;
}


