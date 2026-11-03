// File offset entry: 0xa20e4

/* WARNING: Possible PIC construction at 0x020a2460: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x020a2464) */
/* WARNING: Removing unreachable block (ram,0x020a246c) */
/* WARNING: Removing unreachable block (ram,0x020a249c) */
/* WARNING: Removing unreachable block (ram,0x020a24bc) */
/* WARNING: Removing unreachable block (ram,0x020a24dc) */
/* WARNING: Removing unreachable block (ram,0x020a24f8) */
/* WARNING: Removing unreachable block (ram,0x020a250c) */
/* WARNING: Removing unreachable block (ram,0x020a2518) */

int FUN_020a1fe4(undefined8 param_1,undefined4 param_2)

{
  bool bVar1;
  undefined1 uVar2;
  int iVar3;
  ulong uVar4;
  uint uVar5;
  undefined8 uVar6;
  undefined8 uVar7;
  undefined4 *puVar8;
  undefined1 auVar9 [16];
  undefined4 uStack_1a4;
  undefined4 uStack_1a0;
  undefined4 uStack_19c;
  undefined4 uStack_198;
  undefined4 uStack_194;
  undefined4 uStack_190;
  undefined4 uStack_18c;
  long lStack_188;
  ulong uStack_180;
  ulong uStack_178;
  undefined8 uStack_170;
  undefined8 uStack_168;
  undefined8 uStack_160;
  undefined8 uStack_158;
  undefined8 uStack_150;
  undefined8 uStack_148;
  undefined8 uStack_140;
  undefined8 uStack_138;
  undefined8 uStack_130;
  undefined8 uStack_128;
  undefined8 uStack_120;
  undefined8 uStack_118;
  undefined8 uStack_110;
  undefined8 uStack_108;
  undefined8 uStack_100;
  undefined8 uStack_f8;
  undefined8 uStack_f0;
  undefined8 uStack_e8;
  undefined8 uStack_e0;
  undefined8 uStack_d8;
  undefined8 uStack_d0;
  undefined8 uStack_c8;
  undefined8 uStack_c0;
  undefined8 uStack_b8;
  undefined8 uStack_b0;
  undefined8 uStack_a8;
  undefined8 uStack_a0;
  undefined8 uStack_98;
  undefined8 uStack_90;
  undefined8 uStack_88;
  undefined8 uStack_80;
  undefined8 uStack_78;
  long lStack_70;
  
  lStack_70 = *plRam0000000002103280;
  uStack_190 = 0;
  uStack_18c = 0;
  uStack_198 = 0;
  uStack_194 = 0;
  uStack_1a0 = 0;
  uStack_19c = 0;
  uStack_80 = 0;
  uStack_78 = 0;
  uStack_90 = 0;
  uStack_88 = 0;
  uStack_a0 = 0;
  uStack_98 = 0;
  uStack_b0 = 0;
  uStack_a8 = 0;
  uStack_c0 = 0;
  uStack_b8 = 0;
  uStack_d0 = 0;
  uStack_c8 = 0;
  uStack_e0 = 0;
  uStack_d8 = 0;
  uStack_f0 = 0;
  uStack_e8 = 0;
  uStack_100 = 0;
  uStack_f8 = 0;
  uStack_110 = 0;
  uStack_108 = 0;
  uStack_120 = 0;
  uStack_118 = 0;
  uStack_130 = 0;
  uStack_128 = 0;
  uStack_140 = 0;
  uStack_138 = 0;
  uStack_150 = 0;
  uStack_148 = 0;
  uStack_160 = 0;
  uStack_158 = 0;
  uStack_170 = 0;
  uStack_168 = 0;
  func_0x020a4158(&uStack_18c,&uStack_190,&uStack_194,&uStack_198,&uStack_19c,&uStack_1a0);
  iVar3 = func_0x020a7de8(0x21224e0);
  if (iVar3 == 0) {
    bVar1 = iRam00000000021224e0 != 0;
    uRam0000000002122b8c = 0x40;
    if (bVar1) {
      uRam0000000002122b8c = 0x60;
    }
    uRam0000000002122b80 = 0x20;
    if (bVar1) {
      uRam0000000002122b80 = 0x30;
    }
    uRam0000000002122abc = 0x50;
    if (bVar1) {
      uRam0000000002122abc = 0x71;
    }
    iRam0000000002122ac0 = 0x174;
    if (bVar1) {
      iRam0000000002122ac0 = 0x1d6;
    }
    uRam0000000002122b90 = 0x175;
    if (bVar1) {
      uRam0000000002122b90 = 0x1b7;
    }
    uVar4 = func_0x020a2d70();
    if ((uVar4 & 1) == 0) {
      uRam0000000002122b94 = 0x1c4;
      if (iRam00000000021224e0 != 0) {
        uRam0000000002122b94 = 0x247;
      }
      iRam00000000021224e4 = iRam0000000002122ac0 << 1;
      uRam0000000002122b98 = 0x83;
      uRam0000000002122b9c = 0x41d;
      uVar5 = uRam0000000002122abc;
    }
    else {
      uRam0000000002122b94 = 0x339;
      if (iRam00000000021224e0 != 0) {
        uRam0000000002122b94 = 0x3fe;
      }
      iRam00000000021224e4 = uRam0000000002122b90 + uRam0000000002122abc;
      uRam0000000002122b98 = 0x84;
      uRam0000000002122b9c = 0x5d4;
      uVar5 = iRam0000000002122ac0 << 1;
    }
    iRam00000000021224e4 = iRam00000000021224e4 + uVar5;
    FUN_02091664(0x21224e8,0,0x5d4);
    uVar7 = 0x101900;
    func_0x020a0dfc(0x21224e8,0x101900,uRam0000000002122abc);
    if ((uVar4 & 1) == 0) {
      func_0x020a0dfc((ulong)uRam0000000002122abc + 0x21224e8,0x101990,iRam0000000002122ac0);
      uVar6 = 0x101990;
      puVar8 = (undefined4 *)0x2122abc;
    }
    else {
      puVar8 = (undefined4 *)0x2122b90;
      func_0x020a0dfc((ulong)uRam0000000002122abc + 0x21224e8,0x101990,uRam0000000002122b90);
      uVar6 = 0x101b90;
      func_0x020a0dfc((ulong)uRam0000000002122abc + 0x21224e8 + (ulong)uRam0000000002122b90,0x101b90
                      ,iRam0000000002122ac0);
      FUN_02091664(0x101900,0,uRam0000000002122abc);
      uVar7 = 0x101990;
    }
    FUN_02091664(uVar7,0,*puVar8);
    FUN_02091664(uVar6,0,iRam0000000002122ac0);
    FUN_02091664(0x2122ac4,0,0xb9);
    iVar3 = FUN_0209c58c(param_1,param_2,&uStack_b0,iRam00000000021224e0);
    if (iVar3 == 0) {
      func_0x020a0dfc(0x2122ac4,&uStack_b0,uRam0000000002122b80);
      uVar5 = uRam0000000002122b80;
      uStack_178 = 0;
      lStack_188 = (ulong)uRam0000000002122b80 + 0x2122ac4;
      uStack_180 = (ulong)(0xb9 - uRam0000000002122b80);
      func_0x020a3d28(2,&lStack_188);
      func_0x020a3968(0xfffffffffffeee8e,&lStack_188);
      func_0x020a3cd0(0x20c248e,&lStack_188);
      func_0x020a3968(0xfffffffffffeee8d,&lStack_188);
      func_0x020a3968(1,&lStack_188);
      uVar4 = uStack_178;
      if (uStack_178 == 0xffffffffffffffff) {
        iVar3 = 0x10a002;
      }
      else {
        iVar3 = 0x10a002;
        if (uStack_178 <= uStack_180) {
          uStack_1a4 = 0;
          iVar3 = func_0x020a7e6c(&uStack_1a4);
          if (iVar3 == 0) {
            iVar3 = func_0x020a7d10(&uStack_f0,uRam0000000002122b80,uStack_1a4);
            if (iVar3 == 0) {
              uVar5 = uVar5 + (int)uVar4;
              func_0x020a0dfc((ulong)uVar5 + 0x2122ac4,&uStack_f0,uRam0000000002122b80);
              uVar5 = uRam0000000002122b80 + uVar5;
              uVar2 = func_0x0209c3c4();
              *(undefined1 *)((ulong)uVar5 + 0x2122ac4) = uVar2;
              if (uVar5 + 0x41 < 0xba) {
                auVar9._8_8_ = &uStack_130;
                auVar9._0_8_ = &uStack_110;
                goto code_r0x020a252c;
              }
              iVar3 = 0x10a006;
            }
          }
        }
      }
    }
  }
  else {
    iVar3 = 0x10a007;
  }
  FUN_02091664(0x1800d700,0,0x20);
  FUN_02091664(0x1800d720,0,0x20);
  uRam0000000002100e28 = 0xffffffffffffffff;
  iRam00000000021224e4 = iVar3;
  if (*plRam0000000002103280 == lStack_70) {
    return iVar3;
  }
  auVar9 = FUN_02091090(iVar3);
code_r0x020a252c:
  iVar3 = 0x100006;
  if ((auVar9._0_8_ != 0) && (auVar9._8_8_ != 0)) {
    func_0x020a0dfc(auVar9._0_8_,0x1800d700,0x20);
    func_0x020a0dfc(auVar9._8_8_,0x1800d720,0x20);
    iVar3 = 0;
  }
  return iVar3;
}


