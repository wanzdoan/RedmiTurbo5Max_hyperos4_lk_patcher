// File offset entry: 0xd360

undefined8 FUN_ffff0000b800d360(ulong param_1,long param_2)

{
  long lVar1;
  ulong uVar2;
  long lVar3;
  long *plVar4;
  long lVar5;
  uint uVar6;
  ulong *puVar7;
  long lVar8;
  ulong *puVar9;
  uint *puVar10;
  ulong *puVar11;
  ulong *puVar12;
  ulong *puVar13;
  ulong *puVar14;
  ulong *puVar15;
  ulong uVar16;
  uint uVar17;
  undefined8 local_178;
  undefined8 uStack_170;
  undefined8 local_168;
  undefined8 uStack_160;
  undefined8 local_158;
  undefined8 uStack_150;
  undefined8 local_148;
  undefined8 uStack_140;
  undefined8 local_138;
  undefined8 uStack_130;
  undefined8 local_128;
  undefined8 uStack_120;
  undefined8 local_118;
  undefined8 uStack_110;
  undefined8 local_108;
  ulong uStack_100;
  long local_f8;
  long local_f0;
  undefined8 local_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  
  local_108 = 0;
  uStack_100 = 0;
  local_118 = 0;
  uStack_110 = 0;
  local_128 = 0;
  uStack_120 = 0;
  local_138 = 0;
  uStack_130 = 0;
  local_148 = 0;
  uStack_140 = 0;
  local_158 = 0;
  uStack_150 = 0;
  local_168 = 0;
  uStack_160 = 0;
  local_178 = 0;
  uStack_170 = 0;
  if ((DAT_ffff0000b8114504 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b3a4
              (s_lib_mblock_mblock_c_ffff0000b809a47d,0x39a,s_mblock_init_done_ffff0000b80995a8);
  }
  if ((param_2 == 0) || ((((uint)param_2 | (uint)param_1) & 0xffff) != 0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b2b8
              (s__s_size_invalid_size__llx__addr__ffff0000b8085014,
               s_mblock_free_with_size_ffff0000b808b32d,param_2,param_1);
  }
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  puVar7 = (ulong *)(DAT_ffff0000b8114508 + 8);
  local_d0 = 0;
  uStack_c8 = 0;
  puVar9 = puVar7 + (ulong)(DAT_ffff0000b8114548 - 1) * 3;
  local_e0 = 0;
  uStack_d8 = 0;
  local_f0 = 0;
  local_e8 = 0;
  local_f8 = 0;
  if (puVar7 <= puVar9) {
    do {
      uVar2 = *puVar7;
      if (((param_1 >= uVar2 && param_1 <= puVar7[1] + uVar2) &&
           (param_1 < uVar2 || puVar7[1] + uVar2 != param_1)) ||
         ((param_1 < uVar2 && (uVar2 < param_2 + param_1)))) {
        FUN_ffff0000b804beb0
                  (s__s__d__error_addr__llx_overlap_w_ffff0000b8094ecb,
                   s_mblock_free_with_size_ffff0000b808b32d,0x3ac,param_1);
        return 0xffffffff;
      }
      puVar7 = puVar7 + 3;
    } while (puVar7 <= puVar9);
  }
  if (DAT_ffff0000b8115158 == 0) {
    uVar17 = 0;
    uVar6 = 0;
  }
  else {
    uVar17 = 0;
    plVar4 = (long *)(DAT_ffff0000b8114508 + 0xc18);
    do {
      if ((*plVar4 - 1U < param_1) &&
         (uVar6 = uVar17, param_2 + param_1 <= (ulong)(plVar4[1] + *plVar4)))
      goto joined_r0xffff0000b800d474;
      uVar17 = uVar17 + 1;
      plVar4 = plVar4 + 0x13;
    } while (plVar4 <= (long *)(DAT_ffff0000b8114508 + 0x5780U));
    uVar6 = 0;
joined_r0xffff0000b800d474:
    if (uVar17 == 0x80) {
      FUN_ffff0000b804beb0
                (s__s__d__error__addr_0x_llx_size_0_ffff0000b809e28d,
                 s_mblock_free_with_size_ffff0000b808b32d,0x3c3,param_1,param_2);
      return 0xffffffff;
    }
  }
  uVar16 = (ulong)uVar6;
  lVar5 = DAT_ffff0000b8114508 + (ulong)uVar6 * 0x98 + 0xc2c;
  uVar2 = FUN_ffff0000b804cf40(lVar5);
  if (uVar2 < 0x80) {
    FUN_ffff0000b804c8b4(&local_178,lVar5,uVar2);
    lVar5 = DAT_ffff0000b8114508;
    lVar3 = FUN_ffff0000b804cf40(DAT_ffff0000b8114508 + uVar16 * 0x98 + 0xc2c);
    *(undefined1 *)((long)&local_178 + lVar3) = 0;
  }
  else {
    FUN_ffff0000b804c8b4(&local_178,lVar5,0x7f);
    uStack_100 = uStack_100 & 0xffffffffffffff;
    lVar5 = DAT_ffff0000b8114508;
  }
  lVar3 = lVar5 + uVar16 * 0x98;
  uVar2 = *(ulong *)(lVar3 + 0xc18);
  lVar8 = *(long *)(lVar3 + 0xc20);
  if ((uVar2 == param_1) && (lVar8 == param_2)) {
    FUN_ffff0000b804cbe4(lVar5 + (ulong)uVar17 * 0x98 + 0xc18,0,0x98);
    DAT_ffff0000b8115158 = DAT_ffff0000b8115158 - 1;
  }
  else {
    if (uVar2 == param_1) {
      *(ulong *)(lVar3 + 0xc18) = param_1 + param_2;
    }
    else {
      lVar1 = (lVar8 + uVar2) - (param_2 + param_1);
      if (lVar1 != 0) {
        if (0x7f < DAT_ffff0000b8115158) {
          FUN_ffff0000b804beb0
                    (s__s__d__error_reserved_num_reach_t_ffff0000b808e82c,
                     s_mblock_free_with_size_ffff0000b808b32d,0x3e6);
          return 0xffffffff;
        }
        lVar5 = lVar5 + uVar16 * 0x98;
        local_e8 = CONCAT44(local_e8._4_4_,*(undefined4 *)(lVar5 + 0xc28));
        local_f8 = param_2 + param_1;
        local_f0 = lVar1;
        FUN_ffff0000b804c8b4((long)&local_e8 + 4,lVar5 + 0xc2c,0x80);
        lVar3 = DAT_ffff0000b8114508;
        lVar5 = 0;
        plVar4 = (long *)(DAT_ffff0000b8114508 + 0xc18);
        do {
          if (*plVar4 == 0) {
            if (lVar5 != 0xffffffff) {
              FUN_ffff0000b804c8b4(plVar4,&local_f8,0x98);
              DAT_ffff0000b8115158 = DAT_ffff0000b8115158 + 1;
              *(ulong *)(lVar3 + uVar16 * 0x98 + 0xc20) = param_1 - uVar2;
              goto LAB_ffff0000b800d5f4;
            }
            break;
          }
          lVar5 = lVar5 + 1;
          plVar4 = plVar4 + 0x13;
        } while (lVar5 != 0x80);
        FUN_ffff0000b804beb0
                  (s__s__d___s_error__No_empty_reserv_ffff0000b808b303,
                   s_mblock_free_with_size_ffff0000b808b32d,0x3f5,
                   s_mblock_free_with_size_ffff0000b808b32d);
        return 0xffffffff;
      }
    }
    *(long *)(lVar3 + 0xc20) = lVar8 - param_2;
  }
LAB_ffff0000b800d5f4:
  FUN_ffff0000b804beb0
            (s__s__d__start_0x_llx_size__0x_llx_ffff0000b80888e9,
             s_mblock_free_with_size_ffff0000b808b32d,0x408,param_1,param_2,&local_178);
  if (*DAT_ffff0000b8114510 != 0) {
    uVar2 = 0;
    puVar10 = DAT_ffff0000b8114510;
    do {
      if ((*(ulong *)(puVar10 + 2) <= param_1) &&
         (param_2 + param_1 <= *(long *)(puVar10 + 4) + *(ulong *)(puVar10 + 2)))
      goto LAB_ffff0000b800d66c;
      uVar2 = uVar2 + 1;
      puVar10 = puVar10 + 4;
    } while (*DAT_ffff0000b8114510 != uVar2);
  }
  uVar2 = 0;
LAB_ffff0000b800d66c:
  puVar7 = (ulong *)(DAT_ffff0000b8114508 + 8);
  puVar9 = puVar7 + (ulong)(DAT_ffff0000b8114548 - 1) * 3;
  if (puVar9 < puVar7) {
LAB_ffff0000b800d68c:
    uVar17 = 0;
LAB_ffff0000b800d690:
    if (DAT_ffff0000b8114548 - uVar17 != 0) {
      FUN_ffff0000b804c9cc
                (DAT_ffff0000b8114508 + 8 + (ulong)(uVar17 + 1) * 0x18,
                 DAT_ffff0000b8114508 + 8 + (ulong)uVar17 * 0x18,
                 (ulong)(DAT_ffff0000b8114548 - uVar17) * 0x18);
    }
    lVar5 = DAT_ffff0000b8114508 + (ulong)uVar17 * 0x18;
    *(ulong *)(lVar5 + 8) = param_1;
    *(long *)(lVar5 + 0x10) = param_2;
    *(int *)(lVar5 + 0x18) = (int)uVar2;
    *(undefined4 *)(lVar5 + 0x1c) = 0;
    DAT_ffff0000b8114548 = DAT_ffff0000b8114548 + 1;
  }
  else {
    puVar12 = (ulong *)0x0;
    puVar14 = (ulong *)0x0;
    puVar15 = puVar7;
    do {
      puVar11 = puVar12;
      puVar13 = puVar15;
      if ((param_2 + param_1 != *puVar15) &&
         (puVar11 = puVar15, puVar13 = puVar14, puVar15[1] + *puVar15 != param_1)) {
        puVar11 = puVar12;
      }
      puVar15 = puVar15 + 3;
      puVar12 = puVar11;
      puVar14 = puVar13;
    } while (puVar15 <= puVar9);
    if ((puVar13 == (ulong *)0x0) || (puVar11 == (ulong *)0x0)) {
      if (puVar13 == (ulong *)0x0) {
        if (puVar11 == (ulong *)0x0) {
          if (puVar9 < puVar7) goto LAB_ffff0000b800d68c;
          uVar17 = 0;
          do {
            if (param_1 < *puVar7) break;
            uVar17 = uVar17 + 1;
            puVar7 = puVar7 + 3;
          } while (puVar7 <= puVar9);
          goto LAB_ffff0000b800d690;
        }
      }
      else {
        *puVar13 = param_1;
        puVar13[1] = puVar13[1] + param_2;
        if (puVar11 == (ulong *)0x0) goto LAB_ffff0000b800d7ac;
        param_2 = 0;
      }
      uVar2 = puVar11[1] + param_2;
    }
    else {
      if (puVar11[1] <= puVar13[1]) {
        *puVar13 = param_1;
        puVar13[1] = puVar13[1] + param_2;
        goto LAB_ffff0000b800d7ac;
      }
      uVar2 = puVar11[1] + param_2;
    }
    puVar11[1] = uVar2;
  }
LAB_ffff0000b800d7ac:
  FUN_ffff0000b800dee0();
  return 0;
}


