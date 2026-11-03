// ffff00009074c2e4  size=4  FUN_ffff00009074c2e4

void FUN_ffff00009074c2e4(int param_1)

{
  ulong uVar1;
  uint uVar2;
  long lVar4;
  long lVar5;
  ulong uVar3;
  
  uVar2 = 0;
  if (param_1 != 0x2b) {
    uVar2 = (uint)(param_1 != 0x2d);
  }
  uVar3 = (ulong)uVar2;
  uVar1 = 1;
  if (param_1 != 0x2b) {
    uVar1 = 2;
  }
  while( true ) {
    if ((*(byte *)((&DAT_ffff00009080cd68)[uVar3] + 0x1d4) >> 1 & 1) != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_mt6991_disp_dd_ffff0000907f2eb5,0x2e6,
                 s_DSI_REG_i__>DSI_PHY_LD0CON_L0_UL_ffff0000907ee48f);
    }
    if ((*(byte *)((&DAT_ffff00009080cd68)[uVar3] + 0x1d0) >> 1 & 1) != 0) break;
    lVar4 = *(long *)(s_0123456789abcdef_ffff000090804a48 + uVar3 * 8 + 0x10);
    *(uint *)(lVar4 + 0xc) = *(uint *)(lVar4 + 0xc) & 0xffff7fff;
    *(uint *)(lVar4 + 0x1d4) = *(uint *)(lVar4 + 0x1d4) | 8;
    *(uint *)(lVar4 + 0x1d4) = *(uint *)(lVar4 + 0x1d4) | 2;
    *(uint *)(lVar4 + 0x1d0) = *(uint *)(lVar4 + 0x1d0) | 2;
    do {
    } while ((*(uint *)(lVar4 + 0xc) >> 0xf & 1) == 0);
    *(uint *)(lVar4 + 0xc) = *(uint *)(lVar4 + 0xc) & 0xffff7fff;
    lVar5 = (&DAT_ffff00009080cd50)[uVar3];
    uVar3 = uVar3 + 1;
    *(uint *)(lVar5 + 0x160) = *(uint *)(lVar5 + 0x160) & 0xfffffffe;
    *(uint *)(lVar5 + 0x180) = *(uint *)(lVar5 + 0x180) & 0xfffffffe;
    *(uint *)(lVar5 + 0x260) = *(uint *)(lVar5 + 0x260) & 0xfffffffe;
    *(uint *)(lVar5 + 0x280) = *(uint *)(lVar5 + 0x280) & 0xfffffffe;
    *(uint *)(lVar5 + 0x360) = *(uint *)(lVar5 + 0x360) & 0xfffffffe;
    *(uint *)(lVar5 + 0x380) = *(uint *)(lVar5 + 0x380) & 0xfffffffe;
    *(uint *)(lVar5 + 0x460) = *(uint *)(lVar5 + 0x460) & 0xfffffffe;
    *(uint *)(lVar5 + 0x480) = *(uint *)(lVar5 + 0x480) & 0xfffffffe;
    *(uint *)(lVar5 + 0x560) = *(uint *)(lVar5 + 0x560) & 0xfffffffe;
    *(uint *)(lVar5 + 0x580) = *(uint *)(lVar5 + 0x580) & 0xfffffffe;
    *(uint *)(lVar5 + 0x760) = *(uint *)(lVar5 + 0x760) & 0xfffffffe;
    *(uint *)(lVar5 + 0x780) = *(uint *)(lVar5 + 0x780) & 0xfffffffe;
    *(undefined4 *)(lVar5 + 0x164) = 0;
    *(undefined4 *)(lVar5 + 0x184) = 0;
    *(undefined4 *)(lVar5 + 0x264) = 0;
    *(undefined4 *)(lVar5 + 0x284) = 0;
    *(undefined4 *)(lVar5 + 0x364) = 0;
    *(undefined4 *)(lVar5 + 900) = 0;
    *(undefined4 *)(lVar5 + 0x464) = 0;
    *(undefined4 *)(lVar5 + 0x484) = 0;
    *(undefined4 *)(lVar5 + 0x564) = 0;
    *(undefined4 *)(lVar5 + 0x584) = 0;
    *(undefined4 *)(lVar5 + 0x764) = 0;
    *(undefined4 *)(lVar5 + 0x784) = 0;
    *(undefined4 *)(lVar5 + 0x168) = 0;
    *(undefined4 *)(lVar5 + 0x268) = 0;
    *(undefined4 *)(lVar5 + 0x368) = 0;
    *(undefined4 *)(lVar5 + 0x468) = 0;
    *(undefined4 *)(lVar5 + 0x568) = 0;
    *(undefined4 *)(lVar5 + 0x768) = 0;
    *(undefined4 *)(lVar5 + 0x16c) = 0;
    *(undefined4 *)(lVar5 + 0x26c) = 0;
    *(undefined4 *)(lVar5 + 0x36c) = 0;
    *(undefined4 *)(lVar5 + 0x46c) = 0;
    *(undefined4 *)(lVar5 + 0x56c) = 0;
    *(undefined4 *)(lVar5 + 0x76c) = 0;
    *(undefined4 *)(lVar5 + 0x25c) = 0x101;
    *(undefined4 *)(lVar5 + 0x45c) = 0x101;
    *(undefined4 *)(lVar5 + 0x15c) = 0x101;
    *(undefined4 *)(lVar5 + 0x55c) = 0x101;
    *(undefined4 *)(lVar5 + 0x35c) = 0x101;
    *(undefined4 *)(lVar5 + 0x75c) = 0x101;
    *(uint *)(lVar4 + 0x38) = *(uint *)(lVar4 + 0x38) & 0xffffffc3;
    if (uVar3 == uVar1) {
      return;
    }
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_mt6991_disp_dd_ffff0000907f2eb5,0x2e7,
             s_DSI_REG_i__>DSI_PHY_LCCON_LC_ULP_ffff0000907e3173);
}


