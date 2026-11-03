// ffff000090706710  size=1100  FUN_ffff000090706710

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090706710(undefined8 param_1,undefined4 *param_2,uint param_3)

{
  uint uVar1;
  int iVar2;
  uint uVar3;
  undefined1 auVar4 [16];
  long lVar5;
  long lVar6;
  int iVar7;
  int iVar8;
  char *pcVar9;
  undefined8 uVar10;
  undefined *puVar11;
  int iVar12;
  undefined4 uVar13;
  undefined4 *puVar14;
  ulong uVar15;
  short local_68 [2];
  uint local_64;
  
  uVar15 = (ulong)param_3;
  local_68[0] = 0;
  if (param_3 < 2) {
    if ((&DAT_ffff000090816950)[uVar15 * 0x2e] == 0) {
      FUN_ffff00009071f504
                (&DAT_ffff000090816928 + uVar15 * 0xb8,
                 *(undefined4 *)(&DAT_ffff000090816994 + uVar15 * 0xb8));
    }
    *(undefined4 *)((&DAT_ffff000090816948)[uVar15 * 0x17] + 0xc) = 0xdf847;
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d__bat_idx__u__BUG__ffff0000907f9207,
               s_check_and_init_gauge_cust_data_ffff0000907ef30c,0x80,uVar15);
  }
  lVar5 = uVar15 * 0xb8;
  puVar11 = (&PTR_s_mt6379_bat1_ffff0000908168d0)[uVar15];
  uVar3 = *(uint *)(&DAT_ffff0000908168f0 + lVar5);
  iVar2 = *(int *)(&DAT_ffff0000908168ec + lVar5);
  iVar12 = *(int *)(&DAT_ffff0000908168f4 + lVar5);
  FUN_ffff00009077e11c
            (s__s__d___s___dts_car_tune___d__r__ffff0000907d08ff,
             s_mt6379_fg_get_current_ffff0000907cf048,0x196,puVar11,iVar2,uVar3,iVar12);
  if ((_DAT_ffff00009083d090 == 0) &&
     (_DAT_ffff00009083d090 = FUN_ffff000090737168(s_mt6379_ffff0000907ea569),
     _DAT_ffff00009083d090 == 0)) {
    return -7;
  }
  lVar6 = _DAT_ffff00009083d090;
  local_64 = 0;
  puVar14 = (undefined4 *)(&DAT_ffff00009081697c + uVar15 * 0xb8);
  uVar13 = *puVar14;
  iVar7 = FUN_ffff000090737554(_DAT_ffff00009083d090,uVar13,&local_64);
  if (-1 < iVar7) {
    local_64 = local_64 | 1;
    iVar7 = FUN_ffff00009073751c(lVar6,uVar13);
    if (iVar7 == 0) {
      iVar7 = 0x3e9;
      do {
        local_64 = 0;
        if (((_DAT_ffff00009083d090 == 0) &&
            (_DAT_ffff00009083d090 = FUN_ffff000090737168(s_mt6379_ffff0000907ea569),
            _DAT_ffff00009083d090 == 0)) ||
           ((iVar8 = FUN_ffff000090737554
                               (_DAT_ffff00009083d090,
                                *(undefined4 *)(&DAT_ffff000090816978 + uVar15 * 0xb8),&local_64),
            -1 < iVar8 && ((local_64 >> 7 & 1) != 0)))) goto LAB_ffff000090706944;
        iVar7 = iVar7 + -1;
      } while (iVar7 != 0);
      FUN_ffff00009077e11c
                (s__s__d___s_fg_adc_is_data_ready_t_ffff0000907f5c0b,
                 s_mt6379_fg_get_current_ffff0000907cf048,0x1a7,puVar11);
LAB_ffff000090706944:
      iVar7 = FUN_ffff0000907375c4
                        (_DAT_ffff00009083d090,
                         *(undefined4 *)(&DAT_ffff000090816988 + uVar15 * 0xb8),local_68,2);
      lVar6 = _DAT_ffff00009083d090;
      if (iVar7 == 0) {
        uVar13 = *puVar14;
        local_64 = 0;
        iVar7 = FUN_ffff000090737554(_DAT_ffff00009083d090,uVar13,&local_64);
        if (iVar7 < 0) {
          return iVar7;
        }
        local_64 = local_64 | 8;
        iVar7 = FUN_ffff00009073751c(lVar6,uVar13);
        if (iVar7 != 0) {
          return iVar7;
        }
        iVar7 = FUN_ffff000090706b5c(_DAT_ffff00009083d090,*puVar14,1);
        if (iVar7 != 0) {
          return iVar7;
        }
        iVar7 = 0x3e9;
        do {
          local_64 = 0;
          if (((_DAT_ffff00009083d090 != 0) ||
              (_DAT_ffff00009083d090 = FUN_ffff000090737168(s_mt6379_ffff0000907ea569),
              _DAT_ffff00009083d090 != 0)) &&
             ((iVar8 = FUN_ffff000090737554
                                 (_DAT_ffff00009083d090,
                                  *(undefined4 *)(&DAT_ffff000090816978 + uVar15 * 0xb8),&local_64),
              iVar8 < 0 || ((local_64 >> 7 & 1) == 0)))) goto LAB_ffff000090706a5c;
          iVar7 = iVar7 + -1;
        } while (iVar7 != 0);
        FUN_ffff00009077e11c
                  (s__s__d___s_fg_adc_is_data_ready_t_ffff0000907d8b98,
                   s_mt6379_fg_get_current_ffff0000907cf048,0x1bf,puVar11,0);
LAB_ffff000090706a5c:
        iVar7 = FUN_ffff000090706b5c(_DAT_ffff00009083d090,*puVar14,8);
        if (iVar7 != 0) {
          return iVar7;
        }
        uVar1 = 0;
        if (local_68[0] != 0) {
          uVar1 = (int)local_68[0] ^ (uint)(int)local_68[0] >> 0xf;
        }
        *(bool *)param_1 = 0 < local_68[0];
        auVar4 = SEXT816((long)(int)(uVar1 & 0xffff) * (long)iVar12) * SEXT816(0x29f16b11c6d1e109);
        uVar1 = *(uint *)(&DAT_ffff0000908168f0 + lVar5);
        iVar12 = (int)(auVar4._8_8_ >> 0xe) - (auVar4._12_4_ >> 0x1f);
        if ((0x32 < uVar1) || ((1L << ((ulong)uVar1 & 0x3f) & 0x4000000100420U) == 0)) {
          iVar7 = iVar12 * 0x32;
          iVar12 = 0;
          if (uVar3 != 0) {
            iVar12 = iVar7 / (int)uVar3;
          }
        }
        uVar13 = (undefined4)(((long)iVar2 * (long)iVar12) / 1000);
        if ((long)iVar2 * (long)iVar12 - 0x1f400000000U < 0xfffffc17fffffc19) {
          FUN_ffff00009077e11c
                    (s__s__d__dvalue__lld__BUG__maybe_o_ffff0000907ed9ed,
                     s_mt6379_fg_get_current_ffff0000907cf048,0x1e6);
          uVar13 = 0;
        }
        *param_2 = uVar13;
        return 0;
      }
      pcVar9 = s__s__d__Failed_to_read__s_CIC1_ffff0000907f0bff;
      uVar10 = 0x1ae;
      goto LAB_ffff0000907068f0;
    }
  }
  pcVar9 = s__s__d__Failed_to_enable__s_sw_re_ffff0000907cf05e;
  uVar10 = 0x19e;
LAB_ffff0000907068f0:
  FUN_ffff00009077e11c(pcVar9,s_mt6379_fg_get_current_ffff0000907cf048,uVar10,puVar11);
  return iVar7;
}


