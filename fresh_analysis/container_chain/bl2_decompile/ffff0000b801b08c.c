
void FUN_ffff0000b801b08c(undefined4 param_1,undefined4 *param_2)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  bool bVar7;
  ulong uVar8;
  uint uVar9;
  uint uVar10;
  long lVar11;
  uint uVar12;
  uint uVar13;
  
  switch(param_1) {
  case 0:
  case 0x1c:
  case 0x5c:
  case 0x78:
  case 0x9c:
  case 0xdc:
    DAT_ffff0000b80e8f80 = DAT_ffff0000b80e8f80 + 1;
    (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] = *param_2;
    return;
  default:
    goto code_r0xffff0000b801b4bc;
  case 8:
    uVar9 = DAT_ffff0000b80e8f80 - 1;
    uVar10 = DAT_ffff0000b80e8f80 - 2;
    DAT_ffff0000b80e8f80 = DAT_ffff0000b80e8f80 - 3;
    uVar9 = (&DAT_ffff0000b80e8df0)[uVar9];
    uVar10 = (&DAT_ffff0000b80e8df0)[uVar10];
    if (uVar9 < 0x1f) {
      if (uVar9 < uVar10) {
        uVar12 = 0;
      }
      else {
        uVar5 = (uVar9 + 1) - uVar10;
        if (uVar5 < 2) {
          uVar12 = 0;
          uVar3 = uVar10;
        }
        else {
          uVar4 = uVar5 & 0xfffffffe;
          uVar13 = 0;
          uVar12 = 0;
          uVar3 = uVar10 + uVar4;
          uVar2 = uVar10;
          uVar6 = uVar4;
          do {
            uVar1 = uVar2 + 1;
            uVar13 = 1 << (ulong)(uVar2 & 0x1f) | uVar13;
            uVar6 = uVar6 - 2;
            uVar2 = uVar2 + 2;
            uVar12 = 1 << (ulong)(uVar1 & 0x1f) | uVar12;
          } while (uVar6 != 0);
          uVar12 = uVar12 | uVar13;
          if (uVar5 == uVar4) goto code_r0xffff0000b801b550;
        }
        do {
          uVar5 = uVar3 + 1;
          uVar12 = 1 << (ulong)(uVar3 & 0x1f) | uVar12;
          uVar3 = uVar5;
        } while (uVar9 + 1 != uVar5);
      }
    }
    else {
      uVar12 = 0xffffffff;
    }
code_r0xffff0000b801b550:
    (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] =
         (*(uint *)((ulong)(uint)(&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] | 0xffff000000000000)
         & uVar12) >> (ulong)(uVar10 & 0x1f);
    return;
  case 0x10:
    uVar8 = (ulong)DAT_ffff0000b80e8f80;
    DAT_ffff0000b80e8f80 = DAT_ffff0000b80e8f80 - 1;
    (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] =
         (uint)(&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] >>
         (ulong)((&DAT_ffff0000b80e8df0)[uVar8] & 0x1f);
    return;
  case 0x18:
    lVar11 = (ulong)(DAT_ffff0000b80e8f80 - 1) * 4;
    bVar7 = (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80 - 1] ==
            (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80];
    break;
  case 0x20:
    uVar10 = (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80];
    uVar8 = (ulong)(DAT_ffff0000b80e8f80 - 1);
    uVar9 = 0;
    if (uVar10 != 0) {
      uVar9 = 0;
      if (uVar10 != 0) {
        uVar9 = (uint)(&DAT_ffff0000b80e8df0)[uVar8] / uVar10;
      }
    }
LAB_ffff0000b801b448:
    DAT_ffff0000b80e8f80 = DAT_ffff0000b80e8f80 - 1;
    (&DAT_ffff0000b80e8df0)[uVar8] = uVar9;
    return;
  case 0x28:
  case 0x38:
  case 0x3d:
  case 0xbd:
  case 200:
  case 0xdd:
    DAT_ffff0000b80e8f80 = DAT_ffff0000b80e8f80 + 1;
    (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] = 0;
    return;
  case 0x30:
    uVar8 = (ulong)(DAT_ffff0000b80e8f80 - 1);
    uVar9 = (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80];
    uVar10 = (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80 - 1];
    if (uVar10 < uVar9) goto LAB_ffff0000b801b448;
    goto LAB_ffff0000b801b4b4;
  case 0x34:
    uVar8 = (ulong)DAT_ffff0000b80e8f80;
    DAT_ffff0000b80e8f80 = DAT_ffff0000b80e8f80 - 1;
    (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] =
         (uint)((uint)(&DAT_ffff0000b80e8df0)[uVar8] <
               (uint)(&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80]);
    return;
  case 0x40:
    uVar8 = (ulong)DAT_ffff0000b80e8f80;
    DAT_ffff0000b80e8f80 = DAT_ffff0000b80e8f80 - 1;
    uVar9 = 0;
    if ((&DAT_ffff0000b80e8df0)[uVar8] != 0) {
      uVar9 = (uint)(&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] /
              (uint)(&DAT_ffff0000b80e8df0)[uVar8];
    }
    (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] = uVar9;
    return;
  case 0x48:
    uVar8 = (ulong)DAT_ffff0000b80e8f80;
    DAT_ffff0000b80e8f80 = DAT_ffff0000b80e8f80 - 1;
    (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] =
         (uint)((&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] == (&DAT_ffff0000b80e8df0)[uVar8]);
    return;
  case 0x50:
    lVar11 = (ulong)(DAT_ffff0000b80e8f80 - 1) * 4;
    bVar7 = (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80 - 1] == 0 &&
            (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] == 0;
    break;
  case 0x54:
    uVar8 = (ulong)DAT_ffff0000b80e8f80;
    DAT_ffff0000b80e8f80 = DAT_ffff0000b80e8f80 - 1;
    (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] =
         (uint)((uint)(&DAT_ffff0000b80e8df0)[uVar8] <=
               (uint)(&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80]);
    return;
  case 0x60:
    uVar8 = (ulong)DAT_ffff0000b80e8f80;
    DAT_ffff0000b80e8f80 = DAT_ffff0000b80e8f80 - 1;
    (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] =
         (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] | (&DAT_ffff0000b80e8df0)[uVar8];
    return;
  case 0x80:
    uVar8 = (ulong)DAT_ffff0000b80e8f80;
    DAT_ffff0000b80e8f80 = DAT_ffff0000b80e8f80 - 1;
    (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] =
         (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] + (&DAT_ffff0000b80e8df0)[uVar8];
    return;
  case 0x90:
    lVar11 = (ulong)(DAT_ffff0000b80e8f80 - 1) * 4;
    bVar7 = (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80 - 1] == 0 ||
            (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] == 0;
    break;
  case 0xa0:
    uVar8 = (ulong)DAT_ffff0000b80e8f80;
    DAT_ffff0000b80e8f80 = DAT_ffff0000b80e8f80 - 1;
    (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] =
         (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] & (&DAT_ffff0000b80e8df0)[uVar8];
    return;
  case 0xb0:
    uVar8 = (ulong)(DAT_ffff0000b80e8f80 - 1);
    uVar9 = (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80];
    uVar10 = (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80 - 1];
    if (uVar9 < uVar10) goto LAB_ffff0000b801b448;
LAB_ffff0000b801b4b4:
    DAT_ffff0000b80e8f80 = DAT_ffff0000b80e8f80 - 1;
    (&DAT_ffff0000b80e8df0)[uVar8] = uVar10;
code_r0xffff0000b801b4bc:
    return;
  case 0xb4:
    uVar8 = (ulong)DAT_ffff0000b80e8f80;
    DAT_ffff0000b80e8f80 = DAT_ffff0000b80e8f80 - 1;
    (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] =
         (uint)((uint)(&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] <
               (uint)(&DAT_ffff0000b80e8df0)[uVar8]);
    return;
  case 0xc0:
    uVar8 = (ulong)DAT_ffff0000b80e8f80;
    DAT_ffff0000b80e8f80 = DAT_ffff0000b80e8f80 - 1;
    (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] =
         (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] * (&DAT_ffff0000b80e8df0)[uVar8];
    return;
  case 0xd4:
    uVar8 = (ulong)DAT_ffff0000b80e8f80;
    DAT_ffff0000b80e8f80 = DAT_ffff0000b80e8f80 - 1;
    (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] =
         (uint)((uint)(&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] <=
               (uint)(&DAT_ffff0000b80e8df0)[uVar8]);
    return;
  case 0xe0:
    uVar8 = (ulong)DAT_ffff0000b80e8f80;
    DAT_ffff0000b80e8f80 = DAT_ffff0000b80e8f80 - 1;
    (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] =
         (&DAT_ffff0000b80e8df0)[DAT_ffff0000b80e8f80] <<
         (ulong)((&DAT_ffff0000b80e8df0)[uVar8] & 0x1f);
    return;
  }
  DAT_ffff0000b80e8f80 = DAT_ffff0000b80e8f80 - 1;
  *(uint *)((long)&DAT_ffff0000b80e8df0 + lVar11) = (uint)!bVar7;
  return;
}


