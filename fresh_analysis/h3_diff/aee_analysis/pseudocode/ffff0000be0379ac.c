
void FUN_ffff0000be0379ac(int param_1)

{
  ulong uVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  uint *puVar5;
  uint uVar6;
  ulong uVar7;
  uint uVar8;
  
  uVar6 = 0;
  uVar2 = DAT_ffff0000be122470 * 2000;
  uVar1 = 1;
  if (param_1 != 0x2b) {
    uVar1 = 2;
  }
  uVar3 = (uVar2 >> 0xd) + 1;
  uVar8 = 0;
  if (param_1 != 0x2b) {
    uVar8 = (uint)(param_1 != 0x2d);
  }
  uVar7 = (ulong)uVar8;
  do {
    lVar4 = (&DAT_ffff0000be0caeb0)[uVar7];
    *(uint *)(lVar4 + 0x160) = *(uint *)(lVar4 + 0x160) & 0xfffffffe;
    *(uint *)(lVar4 + 0x180) = *(uint *)(lVar4 + 0x180) & 0xfffffffe;
    *(uint *)(lVar4 + 0x260) = *(uint *)(lVar4 + 0x260) & 0xfffffffe;
    *(uint *)(lVar4 + 0x280) = *(uint *)(lVar4 + 0x280) & 0xfffffffe;
    *(uint *)(lVar4 + 0x360) = *(uint *)(lVar4 + 0x360) & 0xfffffffe;
    *(uint *)(lVar4 + 0x380) = *(uint *)(lVar4 + 0x380) & 0xfffffffe;
    *(uint *)(lVar4 + 0x460) = *(uint *)(lVar4 + 0x460) & 0xfffffffe;
    *(uint *)(lVar4 + 0x480) = *(uint *)(lVar4 + 0x480) & 0xfffffffe;
    *(uint *)(lVar4 + 0x560) = *(uint *)(lVar4 + 0x560) & 0xfffffffe;
    *(uint *)(lVar4 + 0x580) = *(uint *)(lVar4 + 0x580) & 0xfffffffe;
    *(uint *)(lVar4 + 0x760) = *(uint *)(lVar4 + 0x760) & 0xfffffffe;
    *(uint *)(lVar4 + 0x780) = *(uint *)(lVar4 + 0x780) & 0xfffffffe;
    *(undefined4 *)(lVar4 + 0x164) = 0;
    *(undefined4 *)(lVar4 + 0x184) = 0;
    *(undefined4 *)(lVar4 + 0x264) = 0;
    *(undefined4 *)(lVar4 + 0x284) = 0;
    *(undefined4 *)(lVar4 + 0x364) = 0;
    *(undefined4 *)(lVar4 + 900) = 0;
    *(undefined4 *)(lVar4 + 0x464) = 0;
    *(undefined4 *)(lVar4 + 0x484) = 0;
    *(undefined4 *)(lVar4 + 0x564) = 0;
    *(undefined4 *)(lVar4 + 0x584) = 0;
    *(undefined4 *)(lVar4 + 0x764) = 0;
    *(undefined4 *)(lVar4 + 0x784) = 0;
    *(undefined4 *)(lVar4 + 0x168) = 0;
    *(undefined4 *)(lVar4 + 0x268) = 0;
    *(undefined4 *)(lVar4 + 0x368) = 0;
    *(undefined4 *)(lVar4 + 0x468) = 0;
    *(undefined4 *)(lVar4 + 0x568) = 0;
    *(undefined4 *)(lVar4 + 0x768) = 0;
    *(undefined4 *)(lVar4 + 0x16c) = 0;
    *(undefined4 *)(lVar4 + 0x26c) = 0;
    *(undefined4 *)(lVar4 + 0x36c) = 0;
    *(undefined4 *)(lVar4 + 0x46c) = 0;
    *(undefined4 *)(lVar4 + 0x56c) = 0;
    *(undefined4 *)(lVar4 + 0x76c) = 0;
    *(undefined4 *)(lVar4 + 0x25c) = 0x101;
    *(undefined4 *)(lVar4 + 0x45c) = 0x101;
    *(undefined4 *)(lVar4 + 0x15c) = 0x101;
    *(undefined4 *)(lVar4 + 0x55c) = 0x101;
    *(undefined4 *)(lVar4 + 0x35c) = 0x101;
    *(undefined4 *)(lVar4 + 0x75c) = 0x101;
    if ((&DAT_ffff0000be122910)[uVar7 * 0x2f6] != 0) {
      *(uint *)(*(long *)(s_0123456789abcdef_ffff0000be0c8230 + uVar7 * 8 + 0x10) + 0x614) =
           *(uint *)(*(long *)(s_0123456789abcdef_ffff0000be0c8230 + uVar7 * 8 + 0x10) + 0x614) | 1;
      uVar3 = uVar2 / 0x1c00 + 1;
    }
    if (*(int *)(&DAT_ffff0000be1223c4 + uVar7 * 0xbd8) - 1U < 4) {
      uVar6 = *(uint *)(&DAT_ffff0000be0c81e0 +
                       (long)(int)(*(int *)(&DAT_ffff0000be1223c4 + uVar7 * 0xbd8) - 1U) * 4);
    }
    puVar5 = *(uint **)(s_0123456789abcdef_ffff0000be0c8230 + uVar7 * 8 + 0x10);
    puVar5[0xe] = puVar5[0xe] & 0xffffffc0 | puVar5[0xe] & 3 | (uVar6 & 0xf) << 2;
    puVar5[0xc] = puVar5[0xc] & 0xfffffffb;
    puVar5[0xc] = puVar5[0xc] | 4;
    puVar5[0xc] = puVar5[0xc] & 0xfffffffb;
    *(uint *)(lVar4 + 0x160) = *(uint *)(lVar4 + 0x160) | 1;
    *(uint *)(lVar4 + 0x180) = *(uint *)(lVar4 + 0x180) | 1;
    *(uint *)(lVar4 + 0x260) = *(uint *)(lVar4 + 0x260) | 1;
    *(uint *)(lVar4 + 0x280) = *(uint *)(lVar4 + 0x280) | 1;
    *(uint *)(lVar4 + 0x360) = *(uint *)(lVar4 + 0x360) | 1;
    *(uint *)(lVar4 + 0x380) = *(uint *)(lVar4 + 0x380) | 1;
    *(uint *)(lVar4 + 0x460) = *(uint *)(lVar4 + 0x460) | 1;
    *(uint *)(lVar4 + 0x480) = *(uint *)(lVar4 + 0x480) | 1;
    *(uint *)(lVar4 + 0x560) = *(uint *)(lVar4 + 0x560) | 1;
    *(uint *)(lVar4 + 0x580) = *(uint *)(lVar4 + 0x580) | 1;
    *(uint *)(lVar4 + 0x760) = *(uint *)(lVar4 + 0x760) | 1;
    *(uint *)(lVar4 + 0x780) = *(uint *)(lVar4 + 0x780) | 1;
    puVar5[3] = puVar5[3] & 0xffffffbf;
    puVar5[0x75] = puVar5[0x75] | 8;
    puVar5[0xd] = puVar5[0xd] | 0x100000;
    puVar5[0x80] = puVar5[0x80] & 0xffff0000 | uVar3 & 0xffff;
    *(undefined4 *)(lVar4 + 0x25c) = 0;
    *(undefined4 *)(lVar4 + 0x45c) = 0;
    *(undefined4 *)(lVar4 + 0x15c) = 0;
    *(undefined4 *)(lVar4 + 0x55c) = 0;
    *(undefined4 *)(lVar4 + 0x35c) = 0;
    *(undefined4 *)(lVar4 + 0x75c) = 0;
    *puVar5 = *puVar5 & 0xfffffffb;
    *puVar5 = *puVar5 | 4;
    do {
    } while ((puVar5[3] >> 6 & 1) == 0);
    uVar7 = uVar7 + 1;
    puVar5[3] = puVar5[3] & 0xffffffbf;
    puVar5[0x75] = puVar5[0x75] & 0xfffffff7;
    puVar5[0xd] = puVar5[0xd] & 0xffefffff;
    *puVar5 = *puVar5 & 0xfffffffb;
    puVar5[0xc] = puVar5[0xc] | 1;
    puVar5[0xc] = puVar5[0xc] & 0xfffffffe;
  } while (uVar7 != uVar1);
  return;
}


