
ulong FUN_ffff000090719988
                (ulong param_1,ulong param_2,ulong param_3,ulong param_4,ulong param_5,int param_6,
                long param_7)

{
  ulong uVar1;
  ulong uVar2;
  bool bVar3;
  int iVar4;
  int iVar5;
  long lVar6;
  ulong uVar7;
  ulong uVar8;
  char *pcVar9;
  ulong uVar10;
  long lVar11;
  ulong uVar12;
  ulong uVar13;
  undefined8 uVar14;
  ulong *puVar15;
  long lVar16;
  long lVar17;
  ulong *puVar18;
  uint *puVar19;
  uint uVar20;
  ulong uVar21;
  uint uVar22;
  
  if ((DAT_ffff000090850ca0 == '\x01') &&
     (iVar4 = FUN_ffff00009077f0ac(param_7,s_mb_gsort_ffff0000907fb352), iVar4 != 0)) {
    return 0;
  }
  if (param_7 == 0) {
    FUN_ffff00009077e11c
              (s__s__d__name_is_NULL_ffff0000907d27fc,s_mblock_alloc_range_no_lock_ffff0000907cf2bb,
               0x151);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc(s_lib_mblock_mblock_c_ffff0000907f7d89,0x152,&DAT_ffff0000907d2506);
  }
  if ((DAT_ffff000090848c84 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_lib_mblock_mblock_c_ffff0000907f7d89,0x159,s_mblock_init_done_ffff0000907f5edf);
  }
  lVar6 = FUN_ffff00009077f544(param_7,s_shared_dma_pool_ffff0000907ec125);
  if (lVar6 != 0) {
    lVar6 = FUN_ffff00009077f250(param_7);
    lVar6 = param_7 + lVar6 + -4;
    iVar4 = FUN_ffff00009077f348(&DAT_ffff0000907f0f6b,lVar6,4);
    if ((param_6 != 0) && (iVar4 == 0)) {
      FUN_ffff00009077e11c
                (s__s__d__requester_info__sz__0x_ll_ffff0000907febae,
                 s_shared_dma_pool_sanity_check_ffff0000907fec0d,0x11b,param_1,param_6,param_7,
                 param_2,param_4,param_5);
      FUN_ffff00009077e11c
                (s__s__d__node__s__dma_should_be_se_ffff0000907e5d0a,
                 s_shared_dma_pool_sanity_check_ffff0000907fec0d,0x11c,param_7);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc(s_lib_mblock_mblock_c_ffff0000907f7d89,0x11d,&DAT_ffff0000907d2506);
    }
    iVar5 = FUN_ffff00009077f348(&DAT_ffff0000907fce95,lVar6,4);
    if (iVar5 == 0) {
      if (param_6 != 2) {
        FUN_ffff00009077e11c
                  (s__s__d__requester_info__sz__0x_ll_ffff0000907febae,
                   s_shared_dma_pool_sanity_check_ffff0000907fec0d,0x122,param_1,param_6,param_7,
                   param_2,param_4,param_5);
        FUN_ffff00009077e11c
                  (s__s__d__node__s__cma_should_be_se_ffff0000907f9651,
                   s_shared_dma_pool_sanity_check_ffff0000907fec0d,0x124,param_7);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc(s_lib_mblock_mblock_c_ffff0000907f7d89,0x125,&DAT_ffff0000907d2506);
      }
    }
    else if (iVar4 != 0) {
      FUN_ffff00009077e11c
                (s__s__d__node__s__shared_dma_pool_s_ffff0000907f5f20,
                 s_shared_dma_pool_sanity_check_ffff0000907fec0d,299,param_7);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc(s_lib_mblock_mblock_c_ffff0000907f7d89,300,&DAT_ffff0000907d2506);
    }
  }
  if ((param_1 & 0xffff) == 0) {
    if (param_1 == 0) {
      FUN_ffff00009077e11c
                (s__s__d__Reserved_size_cannot_be_0_ffff0000907ea8e8,
                 s_mblock_alloc_range_no_lock_ffff0000907cf2bb,0x162);
    }
    else if ((param_5 & 0xffff) == 0) {
      if ((param_2 & param_2 - 1) == 0) {
        FUN_ffff00009071c2bc();
        iVar4 = DAT_ffff00009085acd0;
        uVar7 = FUN_ffff00009077f250(param_7);
        if (uVar7 < 0x7f) {
          uVar7 = param_2 + 0xffff;
          bVar3 = param_2 != 0;
          param_2 = 0x10000;
          if (bVar3) {
            param_2 = uVar7 & 0xffffffffffff0000;
          }
          puVar15 = (ulong *)(DAT_ffff000090850c90 + 8);
          puVar18 = puVar15 + (ulong)(iVar4 - 1) * 3;
          if (puVar15 <= puVar18) {
            uVar7 = 0;
            uVar20 = 0;
            uVar22 = 0xffffffff;
            do {
              uVar21 = *puVar15;
              uVar1 = puVar15[1] + uVar21;
              uVar10 = uVar1;
              if (param_4 <= uVar1) {
                uVar10 = param_4;
              }
              if ((((param_5 == 0) || ((uVar21 <= param_5 && (param_5 < uVar1)))) &&
                  (param_1 <= puVar15[1])) &&
                 ((((uVar21 < param_4 && (uVar21 < uVar10)) && (param_3 <= uVar10)) &&
                  ((param_3 < uVar21 || (param_3 + param_1 <= uVar10)))))) {
                uVar7 = uVar10 - param_1 & -param_2;
                if (param_5 != 0) {
                  uVar7 = param_5;
                }
                uVar10 = uVar7 + param_1;
                if (0x100000000 < uVar10) {
                  uVar12 = 0x3f;
                  do {
                    uVar8 = uVar12 - 1;
                    if (uVar12 == 0) break;
                    uVar13 = uVar12 & 0x3f;
                    uVar12 = uVar8;
                  } while (uVar7 >> uVar13 == 0);
                  uVar12 = 0x3f;
                  do {
                    uVar13 = uVar12 - 1;
                    if (uVar12 == 0) break;
                    uVar2 = uVar12 & 0x3f;
                    uVar12 = uVar13;
                  } while (uVar10 - 1 >> uVar2 == 0);
                  if ((int)uVar8 + 1 < (int)uVar13 + 1) goto LAB_ffff000090719c58;
                }
                if (((param_3 <= uVar7 && uVar10 <= uVar1) && uVar21 <= uVar7) && uVar10 <= param_4)
                {
                  uVar22 = uVar20;
                }
              }
LAB_ffff000090719c58:
              uVar20 = uVar20 + 1;
              puVar15 = puVar15 + 3;
            } while (puVar15 <= puVar18);
            if (-1 < (int)uVar22) {
              lVar16 = DAT_ffff000090850c90 + (ulong)uVar22 * 0x18;
              lVar6 = *(long *)(lVar16 + 8);
              uVar1 = *(long *)(lVar16 + 0x10) + lVar6;
              if (param_5 == 0) {
                uVar7 = uVar1;
                if (param_4 <= uVar1) {
                  uVar7 = param_4;
                }
                uVar7 = uVar7 - param_1 & -param_2;
              }
              lVar16 = 0;
              lVar17 = 0x80;
              do {
                if (*(long *)(DAT_ffff000090850c90 + lVar16 + 0xc18) == 0) {
                  if (lVar16 != 0x97ffffff68) {
                    lVar17 = DAT_ffff000090850c90 + lVar16;
                    *(ulong *)(lVar17 + 0xc18) = uVar7;
                    *(ulong *)(lVar17 + 0xc20) = param_1;
                    *(int *)(lVar17 + 0xc28) = param_6;
                    uVar10 = FUN_ffff00009077f250(param_7);
                    if (0x7f < uVar10) {
                      pcVar9 = s__s__d___s__name_length_exceed_ma_ffff0000907feb84;
                      uVar14 = 0x1db;
                      goto LAB_ffff000090719db8;
                    }
                    FUN_ffff00009077f2ac(lVar17 + 0xc2c,param_7,uVar10);
                    lVar17 = DAT_ffff000090850c90;
                    lVar11 = FUN_ffff00009077f250(param_7);
                    *(undefined1 *)(lVar17 + lVar11 + lVar16 + 0xc2c) = 0;
                    DAT_ffff00009085b8e0 = DAT_ffff00009085b8e0 + 1;
                    if (uVar7 - lVar6 == 0) {
                      lVar17 = lVar17 + (ulong)uVar22 * 0x18;
                      *(ulong *)(lVar17 + 8) = lVar6 + param_1;
                      *(ulong *)(lVar17 + 0x10) = *(long *)(lVar17 + 0x10) - param_1;
                      goto code_r0xffff000090719f5c;
                    }
                    uVar10 = uVar7 + param_1;
                    if (*DAT_ffff000090850c98 == 0) goto LAB_ffff000090719ee8;
                    uVar21 = 0;
                    puVar19 = DAT_ffff000090850c98;
                    goto LAB_ffff000090719ec8;
                  }
                  break;
                }
                lVar16 = lVar16 + 0x98;
                lVar17 = lVar17 + -1;
              } while (lVar17 != 0);
              pcVar9 = s__s__d___s_error__No_empty_reserv_ffff0000907dc358;
              uVar14 = 0x1cf;
LAB_ffff000090719db8:
              FUN_ffff00009077e11c
                        (pcVar9,s_mblock_alloc_range_no_lock_ffff0000907cf2bb,uVar14,
                         s_mblock_alloc_range_no_lock_ffff0000907cf2bb);
              goto LAB_ffff000090719abc;
            }
          }
          FUN_ffff00009077e11c
                    (s__s__d__error_cannot_find_a_targe_ffff0000907f962e,
                     s_mblock_alloc_range_no_lock_ffff0000907cf2bb,0x1bb);
        }
        else {
          FUN_ffff00009077e11c
                    (s__s__d__mblock_num__d_reserved_nu_ffff0000907fb35b,
                     s_mblock_alloc_range_no_lock_ffff0000907cf2bb,0x177,iVar4,DAT_ffff00009085b8e0,
                     param_6,param_7);
          FUN_ffff00009077e11c
                    (s__s__d__Error__parameters_might_b_ffff0000907f95ea,
                     s_mblock_alloc_range_no_lock_ffff0000907cf2bb,0x178);
        }
      }
      else {
        FUN_ffff00009077e11c
                  (s__s__d__align_need_to_be_power_of_ffff0000907df621,
                   s_mblock_alloc_range_no_lock_ffff0000907cf2bb,0x16a);
      }
    }
    else {
      FUN_ffff00009077e11c
                (s__s__d__expected_address_not_vali_ffff0000907d5b75,
                 s_mblock_alloc_range_no_lock_ffff0000907cf2bb,0x166);
    }
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d__size_not_align_0x_x_ffff0000907d2811,
               s_mblock_alloc_range_no_lock_ffff0000907cf2bb,0x15e,0x10000);
  }
LAB_ffff000090719abc:
  FUN_ffff00009077e11c
            (s__s__d__requester_info__sz__0x_ll_ffff0000907d0bfe,
             s_mblock_alloc_range_no_lock_ffff0000907cf2bb,0x22e,param_1,param_6,param_7,param_2,
             param_3,param_4,param_5);
  FUN_ffff000090719710(0);
  return 0;
  while (uVar21 = uVar21 + 1, puVar19 = puVar19 + 4, *DAT_ffff000090850c98 != uVar21) {
LAB_ffff000090719ec8:
    if ((*(ulong *)(puVar19 + 2) <= uVar10) &&
       (uVar1 <= *(long *)(puVar19 + 4) + *(ulong *)(puVar19 + 2))) goto LAB_ffff000090719eec;
  }
LAB_ffff000090719ee8:
  uVar21 = 0;
LAB_ffff000090719eec:
  lVar17 = lVar17 + 8;
  uVar20 = DAT_ffff00009085acd0 + ~uVar22;
  *(ulong *)(lVar17 + (ulong)uVar22 * 0x18 + 8) = uVar7 - lVar6;
  FUN_ffff00009077ec38
            (lVar17 + (ulong)(uVar22 + 2) * 0x18,lVar17 + (ulong)(uVar22 + 1) * 0x18,
             (ulong)uVar20 * 0x18);
  lVar6 = DAT_ffff000090850c90 + (ulong)(uVar22 + 1) * 0x18;
  *(ulong *)(lVar6 + 8) = uVar10;
  *(ulong *)(lVar6 + 0x10) = uVar1 - uVar10;
  *(int *)(lVar6 + 0x18) = (int)uVar21;
  *(undefined4 *)(lVar6 + 0x1c) = 0;
  DAT_ffff00009085acd0 = DAT_ffff00009085acd0 + 1;
code_r0xffff000090719f5c:
  FUN_ffff00009077e11c
            (s__s__d__start__0x_llx__sz__0x_llx_ffff0000907e0ce7,
             s_mblock_alloc_range_no_lock_ffff0000907cf2bb,0x226,uVar7,param_1,param_3,param_4);
  FUN_ffff00009077e11c
            (s__s__d__map__d_name__s_ffff0000907edc87,s_mblock_alloc_range_no_lock_ffff0000907cf2bb,
             0x228,param_6,param_7);
  return uVar7;
}


