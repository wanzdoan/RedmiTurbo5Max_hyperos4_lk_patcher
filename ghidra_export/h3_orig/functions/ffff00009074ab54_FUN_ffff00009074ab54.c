// ffff00009074ab54  size=1088  FUN_ffff00009074ab54

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009074ab54(void)

{
  uint uVar1;
  ulong uVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  uint uVar8;
  uint uVar9;
  ulong uVar10;
  long lVar11;
  uint *puVar12;
  ulong uVar13;
  uint local_e4;
  ulong local_e0;
  undefined8 local_d8;
  undefined8 local_d0;
  undefined8 local_c8;
  undefined8 local_c0;
  undefined8 local_b8;
  undefined8 local_b0;
  undefined8 local_a8;
  undefined8 local_a0;
  ulong local_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  ulong local_58;
  
  local_70 = 0;
  uStack_68 = 0;
  local_60 = 0;
  local_58 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  local_98 = 0;
  local_b0 = 0;
  local_a8 = 0;
  local_c0 = 0;
  local_b8 = 0;
  local_d0 = 0;
  local_c8 = 0;
  local_e0 = 0;
  local_d8 = 0;
  FUN_ffff00009077eb88(&local_e0,0,0x90);
  lVar11 = -0xffff6f78897c;
  local_c0 = 0xa54ff53a;
  local_b8 = 0x510e527f;
  uVar1 = (_DAT_ffff000000f00000 >> 4 & 0xf) << 0xc |
          _DAT_ffff000000f00000 >> 8 & 0xf0 | _DAT_ffff000000f00000 >> 8 & 0xf |
          (_DAT_ffff000000f00000 & 0xf) << 8;
  uVar9 = uVar1 - (uVar1 >> 1 & 0x55555555);
  local_e0 = 0;
  local_d8 = 0x6a09e667;
  uVar9 = (uVar9 & 0x33333333) + (uVar9 >> 2 & 0x33333333);
  local_d0 = 0xbb67ae85;
  local_c8 = 0x3c6ef372;
  local_b0 = 0x9b05688c;
  local_a8 = 0x1f83d9ab;
  local_a0 = 0x5be0cd19;
  local_98 = 0;
  uVar9 = ((uVar9 + (uVar9 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18) * -0xffffff + 0x20000000 ^
          0xc1569b3e;
  uVar10 = 0x4e0;
  local_e4 = uVar9;
  do {
    if ((local_98 == 0) && (uVar13 = uVar10 - 0x40, 0x3f < uVar10)) {
      FUN_ffff00009074b014(&local_e0,lVar11);
      lVar11 = lVar11 + 0x40;
LAB_ffff00009074acac:
      local_e0 = local_e0 + 0x200;
    }
    else {
      uVar2 = uVar10;
      if (0x40 - local_98 <= uVar10) {
        uVar2 = 0x40 - local_98;
      }
      FUN_ffff00009077e858((long)&local_90 + local_98,lVar11,uVar2);
      lVar11 = lVar11 + uVar2;
      uVar13 = uVar10 - uVar2;
      local_98 = local_98 + uVar2;
      if (local_98 == 0x40) {
        FUN_ffff00009074b014(&local_e0,&local_90);
        local_98 = 0;
        goto LAB_ffff00009074acac;
      }
    }
    uVar10 = uVar13;
  } while (uVar13 != 0);
  local_e4 = uVar9 ^ uVar1 << 8;
  if (0x40 < local_98) {
    return 4;
  }
  puVar12 = &local_e4;
  uVar10 = 4;
  do {
    if ((local_98 == 0) && (uVar13 = uVar10 - 0x40, 0x3f < uVar10)) {
      FUN_ffff00009074b014(&local_e0,puVar12);
      puVar12 = puVar12 + 0x10;
LAB_ffff00009074ad5c:
      local_e0 = local_e0 + 0x200;
    }
    else {
      uVar2 = uVar10;
      if (0x40 - local_98 <= uVar10) {
        uVar2 = 0x40 - local_98;
      }
      FUN_ffff00009077e858((long)&local_90 + local_98,puVar12,uVar2);
      puVar12 = (uint *)((long)puVar12 + uVar2);
      uVar13 = uVar10 - uVar2;
      local_98 = local_98 + uVar2;
      if (local_98 == 0x40) {
        FUN_ffff00009074b014(&local_e0,&local_90);
        local_98 = 0;
        goto LAB_ffff00009074ad5c;
      }
    }
    uVar10 = uVar13;
  } while (uVar13 != 0);
  if (0x3f < local_98) {
    return 5;
  }
  uVar10 = local_98 + 1;
  local_e0 = local_e0 + local_98 * 8;
  *(undefined1 *)((long)&local_90 + local_98) = 0x80;
  if (uVar10 < 0x39) {
    local_98 = uVar10;
    if (uVar10 == 0x38) goto LAB_ffff00009074af00;
  }
  else {
    if (uVar10 < 0x40) {
      *(undefined1 *)((long)&local_90 + local_98 + 1) = 0;
      uVar10 = local_98 + 2;
      if (local_98 + 2 != 0x40) {
        *(undefined1 *)((long)&local_90 + local_98 + 2) = 0;
        uVar10 = local_98 + 3;
        if (local_98 + 3 != 0x40) {
          *(undefined1 *)((long)&local_90 + local_98 + 3) = 0;
          uVar10 = local_98 + 4;
          if (local_98 + 4 != 0x40) {
            *(undefined1 *)((long)&local_90 + local_98 + 4) = 0;
            uVar10 = local_98 + 5;
            if (local_98 + 5 != 0x40) {
              *(undefined1 *)((long)&local_90 + local_98 + 5) = 0;
              uVar10 = local_98 + 6;
              if (local_98 + 6 != 0x40) {
                *(undefined1 *)((long)&local_90 + local_98 + 6) = 0;
                uVar10 = local_98 + 7;
                if (local_98 + 7 != 0x40) {
                  *(undefined1 *)((long)&local_90 + local_98 + 7) = 0;
                  uVar10 = local_98 + 8;
                }
              }
            }
          }
        }
      }
    }
    local_98 = uVar10;
    FUN_ffff00009074b014(&local_e0,&local_90);
    uVar10 = 0;
  }
  do {
    local_98 = uVar10 + 1;
    *(undefined1 *)((long)&local_90 + uVar10) = 0;
    uVar10 = local_98;
  } while (local_98 != 0x38);
LAB_ffff00009074af00:
  uVar10 = (local_e0 & 0xff00ff00ff00ff00) >> 8 | (local_e0 & 0xff00ff00ff00ff) << 8;
  uVar10 = (uVar10 & 0xffff0000ffff0000) >> 0x10 | (uVar10 & 0xffff0000ffff) << 0x10;
  local_58 = uVar10 >> 0x20 | uVar10 << 0x20;
  FUN_ffff00009074b014(&local_e0,&local_90);
  uVar9 = ((uint)local_d8 & 0xff00ff00) >> 8 | ((uint)local_d8 & 0xff00ff) << 8;
  uVar1 = ((uint)local_d0 & 0xff00ff00) >> 8 | ((uint)local_d0 & 0xff00ff) << 8;
  uVar3 = ((uint)local_c8 & 0xff00ff00) >> 8 | ((uint)local_c8 & 0xff00ff) << 8;
  uVar4 = ((uint)local_c0 & 0xff00ff00) >> 8 | ((uint)local_c0 & 0xff00ff) << 8;
  uVar5 = ((uint)local_b8 & 0xff00ff00) >> 8 | ((uint)local_b8 & 0xff00ff) << 8;
  uVar6 = ((uint)local_b0 & 0xff00ff00) >> 8 | ((uint)local_b0 & 0xff00ff) << 8;
  uVar7 = ((uint)local_a8 & 0xff00ff00) >> 8 | ((uint)local_a8 & 0xff00ff) << 8;
  uVar8 = ((uint)local_a0 & 0xff00ff00) >> 8 | ((uint)local_a0 & 0xff00ff) << 8;
  _DAT_ffff000090877b80 = uVar8 >> 0x10 | uVar8 << 0x10;
  _DAT_ffff000090877b7c = uVar7 >> 0x10 | uVar7 << 0x10;
  _DAT_ffff000090877b78 = uVar6 >> 0x10 | uVar6 << 0x10;
  _DAT_ffff000090877b74 = uVar5 >> 0x10 | uVar5 << 0x10;
  _DAT_ffff000090877b70 = uVar4 >> 0x10 | uVar4 << 0x10;
  _DAT_ffff000090877b6c = uVar3 >> 0x10 | uVar3 << 0x10;
  _DAT_ffff000090877b68 = uVar1 >> 0x10 | uVar1 << 0x10;
  _DAT_ffff000090877b64 = uVar9 >> 0x10 | uVar9 << 0x10;
  return 0;
}


