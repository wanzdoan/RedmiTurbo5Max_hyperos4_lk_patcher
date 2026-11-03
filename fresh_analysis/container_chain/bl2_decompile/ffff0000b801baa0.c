
void FUN_ffff0000b801baa0(long param_1)

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
  
  FUN_ffff0000b804c33c(s_leds_init__20241108_ffff0000b80909fe);
  if (param_1 == 0) {
    FUN_ffff0000b804c33c(s__leds__fdt_null__fall_back_to_de_ffff0000b808ebba);
    DAT_ffff0000b811b268 = DAT_ffff0000b80e9028;
    DAT_ffff0000b811b270 = (undefined8 *)PTR_PTR_ffff0000b80e9030;
    DAT_ffff0000b811b278 = DAT_ffff0000b80e9038;
  }
  else {
    iVar4 = FUN_ffff0000b80470b4(param_1);
    if (iVar4 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b744(s_Bad_DTB_header__ffff0000b8086070);
    }
    if (DAT_ffff0000b811b270 == (undefined8 *)0x0) {
      DAT_ffff0000b811b268 = CONCAT44(DAT_ffff0000b811b268._4_4_,8);
      DAT_ffff0000b811b270 = &DAT_ffff0000b811b280;
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
    DAT_ffff0000b811b278 = param_1;
    do {
      puVar13 = (&PTR_DAT_ffff0000b80e8f88)[uVar14];
      puVar12 = (undefined8 *)((long)DAT_ffff0000b811b270 + lVar15);
      *(int *)(puVar12 + 3) = (int)uVar14;
      *puVar12 = puVar13;
      if ((&DAT_ffff0000b80e8fc8)[uVar14] != 0) {
        FUN_ffff0000b804d428(&local_a8,&DAT_ffff0000b808624e,0x40);
        iVar4 = FUN_ffff0000b8047d28(param_1,(&DAT_ffff0000b80e8fc8)[uVar14]);
        if ((-1 < iVar4) ||
           ((uVar10 = FUN_ffff0000b804d2c8(&local_a8,(&DAT_ffff0000b80e8fc8)[uVar14],0x40),
            uVar10 < 0x40 && (iVar4 = FUN_ffff0000b8047d28(param_1,&local_a8), -1 < iVar4)))) {
          local_ac = 0;
          local_64 = 0;
          puVar7 = (uint *)FUN_ffff0000b804803c
                                     (param_1,iVar4,s_force_dts_ffff0000b808fb01,&local_64);
          uVar11 = 0;
          if ((0 < local_64) && (puVar7 != (uint *)0x0)) {
            uVar11 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
            uVar11 = uVar11 >> 0x10 | uVar11 << 0x10;
          }
          bVar3 = uVar11 != 0;
          local_64 = 0;
          puVar7 = (uint *)FUN_ffff0000b804803c(param_1,iVar4,s_led_mode_ffff0000b809f624,&local_64)
          ;
          uVar11 = 0;
          if ((0 < local_64) && (puVar7 != (uint *)0x0)) {
            uVar11 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
            uVar11 = uVar11 >> 0x10 | uVar11 << 0x10;
          }
          *(uint *)((long)DAT_ffff0000b811b270 + lVar15 + 8) = uVar11;
          local_64 = 0;
          puVar7 = (uint *)FUN_ffff0000b804803c
                                     (param_1,iVar4,s_max_hw_brightness_ffff0000b808fb0b,&local_64);
          uVar11 = 0x3ff;
          if ((0 < local_64) && (puVar7 != (uint *)0x0)) {
            uVar11 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
            uVar11 = uVar11 >> 0x10 | uVar11 << 0x10;
          }
          *(uint *)((long)DAT_ffff0000b811b270 + lVar15 + 0x14) = uVar11;
          lVar8 = FUN_ffff0000b804803c(param_1,iVar4,s_default_state_ffff0000b809d627,&local_ac);
          local_64 = 0;
          FUN_ffff0000b804803c(param_1,iVar4,s_lk_backlight_ffff0000b809d635,&local_64);
          puVar12 = DAT_ffff0000b811b270;
          if (lVar8 == 0) {
            *(undefined4 *)((long)DAT_ffff0000b811b270 + lVar15 + 0xc) = 0x66;
          }
          else {
            uVar9 = FUN_ffff0000b804d3cc(s_cust_lk_backlight_ffff0000b8093341);
            iVar5 = FUN_ffff0000b804d4c4(lVar8,s_cust_lk_backlight_ffff0000b8093341,uVar9);
            if (iVar5 == 0) {
              local_64 = 0;
              puVar7 = (uint *)FUN_ffff0000b804803c
                                         (param_1,iVar4,s_lk_backlight_ffff0000b809d635,&local_64);
              if ((local_64 < 1) || (puVar7 == (uint *)0x0)) {
                FUN_ffff0000b804c33c
                          (s_get__s__d_info_from_dts__state___ffff0000b808d016,
                           (&PTR_DAT_ffff0000b80e8f88)[uVar14],uVar14 & 0xffffffff,lVar8,0);
LAB_ffff0000b801beb0:
                iVar5 = 0x66;
                lVar8 = (long)DAT_ffff0000b811b270 + lVar15;
              }
              else {
                uVar11 = *puVar7;
                uVar1 = (uVar11 & 0xff00ff00) >> 8 | (uVar11 & 0xff00ff) << 8;
                uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
                FUN_ffff0000b804c33c
                          (s_get__s__d_info_from_dts__state___ffff0000b808d016,
                           (&PTR_DAT_ffff0000b80e8f88)[uVar14],uVar14 & 0xffffffff,lVar8,uVar1);
                if (uVar11 == 0) goto LAB_ffff0000b801beb0;
                lVar8 = (long)DAT_ffff0000b811b270 + lVar15;
                iVar5 = 0;
                if (*(int *)(lVar8 + 0x14) != 0) {
                  iVar5 = (int)(uVar1 * 0xff) / *(int *)(lVar8 + 0x14);
                }
              }
              puVar12 = DAT_ffff0000b811b270;
              *(int *)(lVar8 + 0xc) = iVar5;
            }
            else {
              uVar9 = FUN_ffff0000b804d3cc(s_FifteenPercent_ffff0000b80a02be);
              iVar5 = FUN_ffff0000b804d4c4(lVar8,s_FifteenPercent_ffff0000b80a02be,uVar9);
              if (iVar5 == 0) {
                uVar6 = 0x15;
LAB_ffff0000b801bee8:
                lVar8 = (long)DAT_ffff0000b811b270 + lVar15;
              }
              else {
                uVar9 = FUN_ffff0000b804d3cc(&DAT_ffff0000b809e444);
                iVar5 = FUN_ffff0000b804d4c4(lVar8,&DAT_ffff0000b809e444,uVar9);
                if (iVar5 == 0) {
                  uVar6 = 0x7f;
                  goto LAB_ffff0000b801bee8;
                }
                uVar9 = FUN_ffff0000b804d3cc(&DAT_ffff0000b809f64a);
                iVar5 = FUN_ffff0000b804d4c4(lVar8,&DAT_ffff0000b809f64a,uVar9);
                if (iVar5 == 0) {
                  uVar6 = 0xff;
                  goto LAB_ffff0000b801bee8;
                }
                uVar9 = FUN_ffff0000b804d3cc(s_FourteenPercent_ffff0000b808d04c);
                iVar5 = FUN_ffff0000b804d4c4(lVar8,s_FourteenPercent_ffff0000b808d04c,uVar9);
                puVar12 = DAT_ffff0000b811b270;
                lVar8 = (long)DAT_ffff0000b811b270 + lVar15;
                if (iVar5 != 0) {
                  *(undefined4 *)(lVar8 + 0xc) = 0;
                  goto LAB_ffff0000b801bef4;
                }
                uVar6 = 0x26;
              }
              puVar12 = DAT_ffff0000b811b270;
              *(undefined4 *)(lVar8 + 0xc) = uVar6;
            }
          }
LAB_ffff0000b801bef4:
          iVar5 = *(int *)((long)puVar12 + lVar15 + 8);
          *(undefined4 *)((long)puVar12 + lVar15 + 0x10) = 0;
          if (iVar5 == 5) {
            FUN_ffff0000b801c0bc(param_1,iVar4);
            iVar5 = *(int *)((long)DAT_ffff0000b811b270 + lVar15 + 8);
            uVar6 = *(undefined4 *)((long)DAT_ffff0000b811b270 + lVar15 + 0x10);
            puVar12 = DAT_ffff0000b811b270;
          }
          else {
            uVar6 = 0;
          }
          FUN_ffff0000b804c33c
                    (s_led__d__offset_is__d_mode_is__d__ffff0000b8096371,uVar14 & 0xffffffff,iVar4,
                     iVar5,uVar6,*(undefined4 *)((long)puVar12 + lVar15 + 0xc),
                     *(undefined4 *)((long)puVar12 + lVar15 + 0x14));
          bVar2 = true;
        }
      }
      uVar14 = uVar14 + 1;
      lVar15 = lVar15 + 0x20;
    } while (lVar15 != 0x100);
    if (!bVar2) {
      DAT_ffff0000b811b268 = DAT_ffff0000b80e9028;
      DAT_ffff0000b811b270 = (undefined8 *)PTR_PTR_ffff0000b80e9030;
      DAT_ffff0000b811b278 = DAT_ffff0000b80e9038;
    }
    if (bVar3) {
      return;
    }
  }
  iVar4 = FUN_ffff0000b803fb64(0);
  if (iVar4 == 0) {
    uVar6 = 6;
  }
  else {
    if (iVar4 != 1) goto LAB_ffff0000b801bf9c;
    uVar6 = 4;
  }
  *(undefined4 *)(DAT_ffff0000b811b270 + 0x19) = uVar6;
LAB_ffff0000b801bf9c:
  uVar6 = FUN_ffff0000b803ea94(0);
  *(undefined4 *)((long)DAT_ffff0000b811b270 + 0xd4) = uVar6;
  return;
}


