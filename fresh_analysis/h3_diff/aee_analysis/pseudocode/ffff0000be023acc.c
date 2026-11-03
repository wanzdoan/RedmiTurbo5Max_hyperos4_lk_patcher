
void FUN_ffff0000be023acc(long param_1)

{
  uint uVar1;
  bool bVar2;
  bool bVar3;
  int iVar4;
  int iVar5;
  undefined4 uVar6;
  uint *puVar7;
  long lVar8;
  undefined8 uVar9;
  ulong uVar10;
  uint uVar11;
  undefined8 *puVar12;
  undefined *puVar13;
  ulong uVar14;
  long lVar15;
  undefined4 local_ac;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  int local_64;
  
  FUN_ffff0000be05fd98(s_leds_init__20241108_ffff0000be0b1cd5);
  if (param_1 == 0) {
    FUN_ffff0000be05fd98(s__leds__fdt_null__fall_back_to_de_ffff0000be0af80d);
    DAT_ffff0000be117a80 = DAT_ffff0000be0d6588;
    DAT_ffff0000be117a88 = (undefined8 *)PTR_PTR_ffff0000be0d6590;
    DAT_ffff0000be117a90 = DAT_ffff0000be0d6598;
  }
  else {
    iVar4 = FUN_ffff0000be055460(param_1);
    if (iVar4 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee24(s_Bad_DTB_header__ffff0000be0a4639);
    }
    if (DAT_ffff0000be117a88 == (undefined8 *)0x0) {
      DAT_ffff0000be117a80 = CONCAT44(DAT_ffff0000be117a80._4_4_,8);
      DAT_ffff0000be117a88 = &DAT_ffff0000be117a98;
    }
    lVar15 = 0;
    uVar14 = 0;
    bVar2 = false;
    bVar3 = false;
    local_78 = 0;
    uStack_70 = 0;
    local_88 = 0;
    uStack_80 = 0;
    local_98 = 0;
    uStack_90 = 0;
    local_a8 = 0;
    uStack_a0 = 0;
    DAT_ffff0000be117a90 = param_1;
    do {
      puVar13 = (&PTR_DAT_ffff0000be0d64e8)[uVar14];
      puVar12 = (undefined8 *)((long)DAT_ffff0000be117a88 + lVar15);
      *(int *)(puVar12 + 3) = (int)uVar14;
      *puVar12 = puVar13;
      if ((&DAT_ffff0000be0d6528)[uVar14] != 0) {
        FUN_ffff0000be060eb8(&local_a8,&DAT_ffff0000be0a465f,0x40);
        iVar4 = FUN_ffff0000be05620c(param_1,(&DAT_ffff0000be0d6528)[uVar14]);
        if ((-1 < iVar4) ||
           ((uVar10 = FUN_ffff0000be060d58(&local_a8,(&DAT_ffff0000be0d6528)[uVar14],0x40),
            uVar10 < 0x40 && (iVar4 = FUN_ffff0000be05620c(param_1,&local_a8), -1 < iVar4)))) {
          local_ac = 0;
          local_64 = 0;
          puVar7 = (uint *)FUN_ffff0000be056494
                                     (param_1,iVar4,s_force_dts_ffff0000be0b0b84,&local_64);
          uVar11 = 0;
          if ((0 < local_64) && (puVar7 != (uint *)0x0)) {
            uVar11 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
            uVar11 = uVar11 >> 0x10 | uVar11 << 0x10;
          }
          bVar3 = uVar11 != 0;
          local_64 = 0;
          puVar7 = (uint *)FUN_ffff0000be056494(param_1,iVar4,s_led_mode_ffff0000be0c3f5c,&local_64)
          ;
          uVar11 = 0;
          if ((0 < local_64) && (puVar7 != (uint *)0x0)) {
            uVar11 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
            uVar11 = uVar11 >> 0x10 | uVar11 << 0x10;
          }
          *(uint *)((long)DAT_ffff0000be117a88 + lVar15 + 8) = uVar11;
          local_64 = 0;
          puVar7 = (uint *)FUN_ffff0000be056494
                                     (param_1,iVar4,s_max_hw_brightness_ffff0000be0b0b8e,&local_64);
          uVar11 = 0x3ff;
          if ((0 < local_64) && (puVar7 != (uint *)0x0)) {
            uVar11 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
            uVar11 = uVar11 >> 0x10 | uVar11 << 0x10;
          }
          *(uint *)((long)DAT_ffff0000be117a88 + lVar15 + 0x14) = uVar11;
          lVar8 = FUN_ffff0000be056494(param_1,iVar4,s_default_state_ffff0000be0c1a36,&local_ac);
          local_64 = 0;
          FUN_ffff0000be056494(param_1,iVar4,s_lk_backlight_ffff0000be0c1a44,&local_64);
          puVar12 = DAT_ffff0000be117a88;
          if (lVar8 == 0) {
            *(undefined4 *)((long)DAT_ffff0000be117a88 + lVar15 + 0xc) = 0x66;
          }
          else {
            uVar9 = FUN_ffff0000be060e5c(s_cust_lk_backlight_ffff0000be0b50d0);
            iVar5 = FUN_ffff0000be060f54(lVar8,s_cust_lk_backlight_ffff0000be0b50d0,uVar9);
            if (iVar5 == 0) {
              local_64 = 0;
              puVar7 = (uint *)FUN_ffff0000be056494
                                         (param_1,iVar4,s_lk_backlight_ffff0000be0c1a44,&local_64);
              if ((local_64 < 1) || (puVar7 == (uint *)0x0)) {
                FUN_ffff0000be05fd98
                          (s_get__s__d_info_from_dts__state___ffff0000be0ad63d,
                           (&PTR_DAT_ffff0000be0d64e8)[uVar14],uVar14 & 0xffffffff,lVar8,0);
LAB_ffff0000be023edc:
                iVar5 = 0x66;
                lVar8 = (long)DAT_ffff0000be117a88 + lVar15;
              }
              else {
                uVar11 = *puVar7;
                uVar1 = (uVar11 & 0xff00ff00) >> 8 | (uVar11 & 0xff00ff) << 8;
                uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
                FUN_ffff0000be05fd98
                          (s_get__s__d_info_from_dts__state___ffff0000be0ad63d,
                           (&PTR_DAT_ffff0000be0d64e8)[uVar14],uVar14 & 0xffffffff,lVar8,uVar1);
                if (uVar11 == 0) goto LAB_ffff0000be023edc;
                lVar8 = (long)DAT_ffff0000be117a88 + lVar15;
                iVar5 = 0;
                if (*(int *)(lVar8 + 0x14) != 0) {
                  iVar5 = (int)(uVar1 * 0xff) / *(int *)(lVar8 + 0x14);
                }
              }
              puVar12 = DAT_ffff0000be117a88;
              *(int *)(lVar8 + 0xc) = iVar5;
            }
            else {
              uVar9 = FUN_ffff0000be060e5c(s_FifteenPercent_ffff0000be0c50db);
              iVar5 = FUN_ffff0000be060f54(lVar8,s_FifteenPercent_ffff0000be0c50db,uVar9);
              if (iVar5 == 0) {
                uVar6 = 0x15;
LAB_ffff0000be023f14:
                lVar8 = (long)DAT_ffff0000be117a88 + lVar15;
              }
              else {
                uVar9 = FUN_ffff0000be060e5c(&DAT_ffff0000be0c2b68);
                iVar5 = FUN_ffff0000be060f54(lVar8,&DAT_ffff0000be0c2b68,uVar9);
                if (iVar5 == 0) {
                  uVar6 = 0x7f;
                  goto LAB_ffff0000be023f14;
                }
                uVar9 = FUN_ffff0000be060e5c(&DAT_ffff0000be0c3f65);
                iVar5 = FUN_ffff0000be060f54(lVar8,&DAT_ffff0000be0c3f65,uVar9);
                if (iVar5 == 0) {
                  uVar6 = 0xff;
                  goto LAB_ffff0000be023f14;
                }
                uVar9 = FUN_ffff0000be060e5c(s_FourteenPercent_ffff0000be0ad673);
                iVar5 = FUN_ffff0000be060f54(lVar8,s_FourteenPercent_ffff0000be0ad673,uVar9);
                puVar12 = DAT_ffff0000be117a88;
                lVar8 = (long)DAT_ffff0000be117a88 + lVar15;
                if (iVar5 != 0) {
                  *(undefined4 *)(lVar8 + 0xc) = 0;
                  goto LAB_ffff0000be023f20;
                }
                uVar6 = 0x26;
              }
              puVar12 = DAT_ffff0000be117a88;
              *(undefined4 *)(lVar8 + 0xc) = uVar6;
            }
          }
LAB_ffff0000be023f20:
          iVar5 = *(int *)((long)puVar12 + lVar15 + 8);
          *(undefined4 *)((long)puVar12 + lVar15 + 0x10) = 0;
          if (iVar5 == 5) {
            FUN_ffff0000be0240e8(param_1,iVar4);
            iVar5 = *(int *)((long)DAT_ffff0000be117a88 + lVar15 + 8);
            uVar6 = *(undefined4 *)((long)DAT_ffff0000be117a88 + lVar15 + 0x10);
            puVar12 = DAT_ffff0000be117a88;
          }
          else {
            uVar6 = 0;
          }
          FUN_ffff0000be05fd98
                    (s_led__d__offset_is__d_mode_is__d__ffff0000be0b892f,uVar14 & 0xffffffff,iVar4,
                     iVar5,uVar6,*(undefined4 *)((long)puVar12 + lVar15 + 0xc),
                     *(undefined4 *)((long)puVar12 + lVar15 + 0x14));
          bVar2 = true;
        }
      }
      uVar14 = uVar14 + 1;
      lVar15 = lVar15 + 0x20;
    } while (lVar15 != 0x100);
    if (!bVar2) {
      DAT_ffff0000be117a80 = DAT_ffff0000be0d6588;
      DAT_ffff0000be117a88 = (undefined8 *)PTR_PTR_ffff0000be0d6590;
      DAT_ffff0000be117a90 = DAT_ffff0000be0d6598;
    }
    if (bVar3) {
      return;
    }
  }
  iVar4 = FUN_ffff0000be048f1c(0);
  if (iVar4 == 0) {
    uVar6 = 6;
  }
  else {
    if (iVar4 != 1) goto LAB_ffff0000be023fc8;
    uVar6 = 4;
  }
  *(undefined4 *)(DAT_ffff0000be117a88 + 0x19) = uVar6;
LAB_ffff0000be023fc8:
  uVar6 = FUN_ffff0000be047e4c(0);
  *(undefined4 *)((long)DAT_ffff0000be117a88 + 0xd4) = uVar6;
  return;
}


