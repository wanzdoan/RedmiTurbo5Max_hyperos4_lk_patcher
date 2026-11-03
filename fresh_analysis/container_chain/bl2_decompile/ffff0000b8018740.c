
void FUN_ffff0000b8018740(void)

{
  ushort uVar1;
  byte *pbVar2;
  byte bVar3;
  uint uVar4;
  int iVar5;
  long lVar6;
  long lVar7;
  undefined8 uVar8;
  char *pcVar9;
  undefined8 uVar10;
  undefined8 uVar11;
  ulong uVar12;
  undefined8 *puVar13;
  ulong uVar14;
  undefined8 *puVar15;
  
  DAT_ffff0000b811b0f8 = FUN_ffff0000b804bccc(0x2000);
  DAT_ffff0000b811b100 = FUN_ffff0000b804bccc(0x90);
  FUN_ffff0000b804d070(DAT_ffff0000b811b0f8,0,0x2000);
  lVar7 = DAT_ffff0000b811b0f8;
  lVar6 = FUN_ffff0000b804a99c(s_gsort_ffff0000b8099a60);
  if (lVar6 == 0) {
    FUN_ffff0000b804c33c(s_Parttion__gsort__is_not_exist__ffff0000b80924be);
  }
  else {
    lVar7 = FUN_ffff0000b804aa30(lVar6,lVar7,0,0x2000);
    if (lVar7 == 0x2000) {
      FUN_ffff0000b804c33c(s_gsort_read_size___zu_ffff0000b8097e3b,0x2000);
    }
    else {
      FUN_ffff0000b804c33c(s_Read_gsort_size__ld_is_not_equal_ffff0000b8099a66,lVar7,0x2000);
    }
  }
  FUN_ffff0000b804a920(lVar6);
  lVar7 = DAT_ffff0000b811b0f8;
  uVar8 = FUN_ffff0000b804d3cc(s_gsort_ffff0000b8099a60);
  iVar5 = FUN_ffff0000b804d4c4(lVar7,s_gsort_ffff0000b8099a60,uVar8);
  if (iVar5 == 0) {
    FUN_ffff0000b804c33c(s_GSORT__magic_str_match_ffff0000b808a8be);
    FUN_ffff0000b804c33c
              (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,s_gsort_version_ffff0000b809c478,8,
               *(undefined1 *)(DAT_ffff0000b811b0f8 + 8));
    if (*(char *)(DAT_ffff0000b811b0f8 + 0x10) != '\0') {
      if (*(char *)(DAT_ffff0000b811b0f8 + 0x10) != '\x0f') {
        FUN_ffff0000b804c33c(s_GSORT__trigger_bit_map_is_missma_ffff0000b808dd86);
        FUN_ffff0000b804c33c
                  (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,
                   s_test_trigger_bitmap_ffff0000b8086190,0x10,
                   *(undefined1 *)(DAT_ffff0000b811b0f8 + 0x10));
        FUN_ffff0000b804c33c
                  (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,
                   s_test_history_bitmap_ffff0000b809f5da,0x11,
                   *(undefined1 *)(DAT_ffff0000b811b0f8 + 0x11));
        return;
      }
      FUN_ffff0000b802c1b8(0);
      FUN_ffff0000b804c33c(s_GSORT__trigger_bit_map_in_ffff0000b809e39c);
      FUN_ffff0000b804c33c
                (s__PL_magic_str_offset_0x_x_val__s_ffff0000b8097ec3,0,DAT_ffff0000b811b0f8);
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,s_gsort_version_ffff0000b809c478,8,
                 *(undefined1 *)(DAT_ffff0000b811b0f8 + 8));
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,s_gsort_size_ffff0000b8097ee5,10,
                 *(undefined1 *)(DAT_ffff0000b811b0f8 + 10));
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,s_global_clk_in_khz_ffff0000b808dd49
                 ,0xc,*(undefined1 *)(DAT_ffff0000b811b0f8 + 0xc));
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,
                 s_test_trigger_bitmap_ffff0000b8086190,0x10,
                 *(undefined1 *)(DAT_ffff0000b811b0f8 + 0x10));
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,
                 s_test_history_bitmap_ffff0000b809f5da,0x11,
                 *(undefined1 *)(DAT_ffff0000b811b0f8 + 0x11));
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,s_retest_count_ffff0000b809706a,0x12
                 ,*(undefined1 *)(DAT_ffff0000b811b0f8 + 0x12));
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,
                 s_test_result_bitmap_ffff0000b809a8fc,0x13,
                 *(undefined1 *)(DAT_ffff0000b811b0f8 + 0x13));
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,s_test_prompter_ffff0000b809620d,
                 0x14,*(undefined1 *)(DAT_ffff0000b811b0f8 + 0x14));
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,s_pattern_offset_ffff0000b8098b94,
                 0x16,*(undefined1 *)(DAT_ffff0000b811b0f8 + 0x16));
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,s_pattern_count_ffff0000b809e3b7,
                 0x18,*(undefined1 *)(DAT_ffff0000b811b0f8 + 0x18));
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,
                 s_pattern_error_bitmap_ffff0000b809c486,0x1c,
                 *(undefined1 *)(DAT_ffff0000b811b0f8 + 0x1c));
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,s_result_offset_ffff0000b80861a4,
                 0x24,*(undefined1 *)(DAT_ffff0000b811b0f8 + 0x24));
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,s_result_count_ffff0000b809621b,0x26
                 ,*(undefined1 *)(DAT_ffff0000b811b0f8 + 0x26));
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,s_result_item_size_ffff0000b80932eb,
                 0x27,*(undefined1 *)(DAT_ffff0000b811b0f8 + 0x27));
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,
                 s_ddr_training_times_ffff0000b808a8d6,0x2c,
                 *(undefined1 *)(DAT_ffff0000b811b0f8 + 0x2c));
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,
                 s_ddr_training_bootflag_ffff0000b809c49b,0x30,
                 *(undefined1 *)(DAT_ffff0000b811b0f8 + 0x30));
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,
                 s_ddr_train_failtimes_ffff0000b808c5e1,0x34,
                 *(undefined1 *)(DAT_ffff0000b811b0f8 + 0x34));
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,s_checksum_ffff0000b808dd5b,0x38,
                 *(undefined1 *)(DAT_ffff0000b811b0f8 + 0x38));
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,s_padding_0__ffff0000b8088007,0x3c,
                 *(undefined1 *)(DAT_ffff0000b811b0f8 + 0x3c));
      FUN_ffff0000b804c33c
                (s__LK__s_offset_0x_x_val_0x_x_ffff0000b8088d6c,s_swap_rank_ffff0000b809d5f2,0x28,
                 *(undefined1 *)(DAT_ffff0000b811b0f8 + 0x28));
      if (*(char *)(DAT_ffff0000b811b0f8 + 0x18) == '\0') {
        puVar13 = &DAT_ffff0000b80e8d60;
        *(undefined1 *)(DAT_ffff0000b811b0f8 + 0x18) = 4;
      }
      else {
        puVar13 = (undefined8 *)
                  (DAT_ffff0000b811b0f8 + (ulong)*(ushort *)(DAT_ffff0000b811b0f8 + 0x16));
      }
      FUN_ffff0000b804c33c(s_GSORT__print_patterns_ffff0000b80861b2);
      uVar12 = (ulong)*(byte *)(DAT_ffff0000b811b0f8 + 0x18);
      if (*(byte *)(DAT_ffff0000b811b0f8 + 0x18) != 0) {
        uVar14 = 0;
        puVar15 = puVar13 + 2;
        do {
          FUN_ffff0000b804c33c
                    (s_GSORT__pattern__d___0x_016llx_0x_ffff0000b8097077,uVar14 & 0xffffffff,
                     puVar15[-2],puVar15[-1],*(undefined4 *)puVar15);
          uVar14 = uVar14 + 1;
          uVar12 = (ulong)*(byte *)(DAT_ffff0000b811b0f8 + 0x18);
          puVar15 = puVar15 + 3;
        } while (uVar14 < uVar12);
      }
      lVar7 = DAT_ffff0000b811b0f8;
      uVar4 = (uint)*(ushort *)(DAT_ffff0000b811b0f8 + 0x16) + (int)uVar12 * 0x18;
      uVar1 = (ushort)uVar4;
      DAT_ffff0000b811b100 =
           DAT_ffff0000b811b0f8 + (ulong)uVar1 +
           (ulong)*(byte *)(DAT_ffff0000b811b0f8 + 0x26) * 0x24;
      *(ushort *)(DAT_ffff0000b811b0f8 + 0x24) = uVar1;
      FUN_ffff0000b804c33c
                (s_GSORT__param__p__result__p__offs_ffff0000b80851e3,lVar7,DAT_ffff0000b811b100,
                 uVar4 & 0xffff);
      if (*(char *)(DAT_ffff0000b811b0f8 + 0x10) == '\0') {
        FUN_ffff0000b804c33c(s_no_specific_test_triggered_ffff0000b8089a9a);
      }
      else {
        FUN_ffff0000b8018cec(puVar13);
      }
      if (*(char *)(DAT_ffff0000b811b0f8 + 0x26) == '\0') {
        FUN_ffff0000b804c33c(s_GSORT__No_error_found_ffff0000b8089ab6);
      }
      else {
        FUN_ffff0000b804c33c(s_GSORT__error_found_save_error_ffff0000b809f5ee);
        if (*(char *)(DAT_ffff0000b811b0f8 + 0x26) != '\0') {
          lVar7 = 0;
          uVar12 = 0;
          do {
            pbVar2 = (byte *)(DAT_ffff0000b811b100 + lVar7);
            uVar8 = *(undefined8 *)(pbVar2 + 0xc);
            uVar10 = *(undefined8 *)(pbVar2 + 0x14);
            uVar11 = *(undefined8 *)(pbVar2 + 0x1c);
            *(ulong *)(DAT_ffff0000b811b0f8 + 0x1c) =
                 *(ulong *)(DAT_ffff0000b811b0f8 + 0x1c) | (long)(1 << (ulong)(*pbVar2 & 0x1f));
            FUN_ffff0000b804c33c
                      (s__0x_016llx_0x_016llx__0x_016llx__ffff0000b808dd64,uVar8,uVar10,uVar11);
            uVar12 = uVar12 + 1;
            lVar7 = lVar7 + 0x24;
          } while (uVar12 < *(byte *)(DAT_ffff0000b811b0f8 + 0x26));
        }
      }
      lVar7 = DAT_ffff0000b811b0f8;
      bVar3 = *(byte *)(DAT_ffff0000b811b0f8 + 0x10);
      pbVar2 = (byte *)(DAT_ffff0000b811b0f8 + 0x11);
      *(undefined1 *)(DAT_ffff0000b811b0f8 + 0x10) = 0;
      *(byte *)(lVar7 + 0x11) = *pbVar2 | bVar3;
      FUN_ffff0000b801868c(lVar7,0x2000,0);
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b8014dbc(1,1);
    }
    pcVar9 = s_GSORT__trigger_bit_map_is_null_ffff0000b80861c9;
  }
  else {
    pcVar9 = s_GSORT__magic_str_mismatch_ffff0000b8099b31;
  }
  FUN_ffff0000b804c33c(pcVar9);
  return;
}


