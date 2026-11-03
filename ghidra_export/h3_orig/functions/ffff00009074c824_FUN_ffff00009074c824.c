// ffff00009074c824  size=812  FUN_ffff00009074c824

void FUN_ffff00009074c824(int param_1,long param_2)

{
  ulong uVar1;
  uint uVar2;
  uint uVar3;
  uint *puVar4;
  uint uVar5;
  ulong uVar6;
  uint uVar7;
  long lVar8;
  
  uVar2 = *(int *)(param_2 + 0x230) * 2000;
  uVar5 = 0;
  uVar7 = 0;
  if (param_1 != 0x2b) {
    uVar7 = (uint)(param_1 != 0x2d);
  }
  uVar6 = (ulong)uVar7;
  uVar7 = (uVar2 >> 0xd) + 1;
  uVar1 = 1;
  if (param_1 != 0x2b) {
    uVar1 = 2;
  }
  do {
    lVar8 = (&DAT_ffff00009080cd50)[uVar6];
    *(uint *)(lVar8 + 0x160) = *(uint *)(lVar8 + 0x160) & 0xfffffffe;
    *(uint *)(lVar8 + 0x180) = *(uint *)(lVar8 + 0x180) & 0xfffffffe;
    *(uint *)(lVar8 + 0x260) = *(uint *)(lVar8 + 0x260) & 0xfffffffe;
    *(uint *)(lVar8 + 0x280) = *(uint *)(lVar8 + 0x280) & 0xfffffffe;
    *(uint *)(lVar8 + 0x360) = *(uint *)(lVar8 + 0x360) & 0xfffffffe;
    *(uint *)(lVar8 + 0x380) = *(uint *)(lVar8 + 0x380) & 0xfffffffe;
    *(uint *)(lVar8 + 0x460) = *(uint *)(lVar8 + 0x460) & 0xfffffffe;
    *(uint *)(lVar8 + 0x480) = *(uint *)(lVar8 + 0x480) & 0xfffffffe;
    *(uint *)(lVar8 + 0x560) = *(uint *)(lVar8 + 0x560) & 0xfffffffe;
    *(uint *)(lVar8 + 0x580) = *(uint *)(lVar8 + 0x580) & 0xfffffffe;
    *(uint *)(lVar8 + 0x760) = *(uint *)(lVar8 + 0x760) & 0xfffffffe;
    *(uint *)(lVar8 + 0x780) = *(uint *)(lVar8 + 0x780) & 0xfffffffe;
    *(undefined4 *)(lVar8 + 0x164) = 0;
    *(undefined4 *)(lVar8 + 0x184) = 0;
    *(undefined4 *)(lVar8 + 0x264) = 0;
    *(undefined4 *)(lVar8 + 0x284) = 0;
    *(undefined4 *)(lVar8 + 0x364) = 0;
    *(undefined4 *)(lVar8 + 900) = 0;
    *(undefined4 *)(lVar8 + 0x464) = 0;
    *(undefined4 *)(lVar8 + 0x484) = 0;
    *(undefined4 *)(lVar8 + 0x564) = 0;
    *(undefined4 *)(lVar8 + 0x584) = 0;
    *(undefined4 *)(lVar8 + 0x764) = 0;
    *(undefined4 *)(lVar8 + 0x784) = 0;
    *(undefined4 *)(lVar8 + 0x168) = 0;
    *(undefined4 *)(lVar8 + 0x268) = 0;
    *(undefined4 *)(lVar8 + 0x368) = 0;
    *(undefined4 *)(lVar8 + 0x468) = 0;
    *(undefined4 *)(lVar8 + 0x568) = 0;
    *(undefined4 *)(lVar8 + 0x768) = 0;
    *(undefined4 *)(lVar8 + 0x16c) = 0;
    *(undefined4 *)(lVar8 + 0x26c) = 0;
    *(undefined4 *)(lVar8 + 0x36c) = 0;
    *(undefined4 *)(lVar8 + 0x46c) = 0;
    *(undefined4 *)(lVar8 + 0x56c) = 0;
    *(undefined4 *)(lVar8 + 0x76c) = 0;
    *(undefined4 *)(lVar8 + 0x25c) = 0x101;
    *(undefined4 *)(lVar8 + 0x45c) = 0x101;
    *(undefined4 *)(lVar8 + 0x15c) = 0x101;
    *(undefined4 *)(lVar8 + 0x55c) = 0x101;
    *(undefined4 *)(lVar8 + 0x35c) = 0x101;
    *(undefined4 *)(lVar8 + 0x75c) = 0x101;
    if (*(int *)(param_2 + 0x6d0) != 0) {
      *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804a48 + uVar6 * 8 + 0x10) + 0x614) =
           *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804a48 + uVar6 * 8 + 0x10) + 0x614) | 1;
      uVar7 = uVar2 / 0x1c00 + 1;
    }
    uVar3 = *(int *)(param_2 + 0x184) - 1;
    if (uVar3 < 4) {
      uVar5 = *(uint *)(&DAT_ffff0000908049f8 + (long)(int)uVar3 * 4);
    }
    puVar4 = *(uint **)(s_0123456789abcdef_ffff000090804a48 + uVar6 * 8 + 0x10);
    puVar4[0xe] = puVar4[0xe] & 0xffffffc0 | puVar4[0xe] & 3 | (uVar5 & 0xf) << 2;
    puVar4[0xc] = puVar4[0xc] & 0xfffffffb;
    puVar4[0xc] = puVar4[0xc] | 4;
    puVar4[0xc] = puVar4[0xc] & 0xfffffffb;
    *(uint *)(lVar8 + 0x160) = *(uint *)(lVar8 + 0x160) | 1;
    *(uint *)(lVar8 + 0x180) = *(uint *)(lVar8 + 0x180) | 1;
    *(uint *)(lVar8 + 0x260) = *(uint *)(lVar8 + 0x260) | 1;
    *(uint *)(lVar8 + 0x280) = *(uint *)(lVar8 + 0x280) | 1;
    *(uint *)(lVar8 + 0x360) = *(uint *)(lVar8 + 0x360) | 1;
    *(uint *)(lVar8 + 0x380) = *(uint *)(lVar8 + 0x380) | 1;
    *(uint *)(lVar8 + 0x460) = *(uint *)(lVar8 + 0x460) | 1;
    *(uint *)(lVar8 + 0x480) = *(uint *)(lVar8 + 0x480) | 1;
    *(uint *)(lVar8 + 0x560) = *(uint *)(lVar8 + 0x560) | 1;
    *(uint *)(lVar8 + 0x580) = *(uint *)(lVar8 + 0x580) | 1;
    *(uint *)(lVar8 + 0x760) = *(uint *)(lVar8 + 0x760) | 1;
    *(uint *)(lVar8 + 0x780) = *(uint *)(lVar8 + 0x780) | 1;
    puVar4[3] = puVar4[3] & 0xffffffbf;
    puVar4[0x75] = puVar4[0x75] | 8;
    puVar4[0xd] = puVar4[0xd] | 0x100000;
    puVar4[0x80] = puVar4[0x80] & 0xffff0000 | uVar7 & 0xffff;
    *(undefined4 *)(lVar8 + 0x25c) = 0;
    *(undefined4 *)(lVar8 + 0x45c) = 0;
    *(undefined4 *)(lVar8 + 0x15c) = 0;
    *(undefined4 *)(lVar8 + 0x55c) = 0;
    *(undefined4 *)(lVar8 + 0x35c) = 0;
    *(undefined4 *)(lVar8 + 0x75c) = 0;
    *puVar4 = *puVar4 & 0xfffffffb;
    *puVar4 = *puVar4 | 4;
    do {
    } while ((puVar4[3] >> 6 & 1) == 0);
    uVar6 = uVar6 + 1;
    puVar4[3] = puVar4[3] & 0xffffffbf;
    puVar4[0x75] = puVar4[0x75] & 0xfffffff7;
    puVar4[0xd] = puVar4[0xd] & 0xffefffff;
    *puVar4 = *puVar4 & 0xfffffffb;
    puVar4[0xc] = puVar4[0xc] | 1;
    puVar4[0xc] = puVar4[0xc] & 0xfffffffe;
  } while (uVar6 != uVar1);
  return;
}


