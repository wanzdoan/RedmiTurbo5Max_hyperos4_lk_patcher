// ffff00009072a73c  size=884  FUN_ffff00009072a73c

void FUN_ffff00009072a73c(long param_1)

{
  bool bVar1;
  int iVar2;
  int iVar3;
  int iVar4;
  uint uVar5;
  int iVar6;
  uint *puVar7;
  ulong uVar8;
  undefined1 *puVar9;
  long lVar10;
  bool bVar11;
  uint uVar12;
  uint uVar13;
  char *pcVar14;
  ulong uVar15;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined4 local_68;
  uint local_64;
  
  if (param_1 != 0) {
    iVar2 = FUN_ffff00009076cb48();
    if (iVar2 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cee0(s_Bad_DTB_header__ffff0000907d0dd2);
    }
    bVar11 = false;
    uVar12 = 0;
    iVar6 = 0;
    iVar2 = 1;
    local_68 = 0;
    local_a8 = 0;
    uStack_a0 = 0;
    local_98 = 0;
    uStack_90 = 0;
    local_88 = 0;
    uStack_80 = 0;
    local_78 = 0;
    uStack_70 = 0;
    uVar15 = 7;
    do {
      iVar4 = FUN_ffff00009076da24(param_1,*(undefined8 *)(uVar15 * 8 + -0xffff6f7e7130));
      FUN_ffff00009077ee50(&local_a8,0,0x40);
      if (iVar4 < 0) {
        FUN_ffff00009077f2ac(&local_a8,&DAT_ffff0000907d0df8,0x3f);
        uVar8 = FUN_ffff00009077f14c(&local_a8,*(undefined8 *)(uVar15 * 8 + -0xffff6f7e7130),0x40);
        if ((uVar8 < 0x40) && (iVar4 = FUN_ffff00009076da24(param_1,&local_a8), -1 < iVar4))
        goto LAB_ffff00009072a820;
      }
      else {
        FUN_ffff00009077f2ac(&local_a8,*(undefined8 *)(uVar15 * 8 + -0xffff6f7e7130),0x3f);
LAB_ffff00009072a820:
        local_64 = 0;
        puVar7 = (uint *)FUN_ffff00009076df9c(param_1,iVar4,s_force_dts_ffff0000907e2a54,&local_64);
        uVar13 = 0;
        if ((0 < (int)local_64) && (puVar7 != (uint *)0x0)) {
          uVar12 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
          uVar13 = uVar12 >> 0x10 | uVar12 << 0x10;
        }
        local_64 = 0;
        puVar7 = (uint *)FUN_ffff00009076df9c(param_1,iVar4,s_led_mode_ffff0000907feecf,&local_64);
        uVar12 = 6;
        if ((0 < (int)local_64) && (puVar7 != (uint *)0x0)) {
          uVar12 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
          uVar12 = uVar12 >> 0x10 | uVar12 << 0x10;
        }
        if (uVar13 != 0) {
          return;
        }
        uVar5 = FUN_ffff00009075ce5c(iVar2);
        local_64 = 0;
        puVar7 = (uint *)FUN_ffff00009076df9c
                                   (param_1,iVar4,s_max_hw_brightness_ffff0000907e2a5e,&local_64);
        uVar13 = 0x3ff;
        if ((0 < (int)local_64) && (puVar7 != (uint *)0x0)) {
          uVar13 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
          uVar13 = uVar13 >> 0x10 | uVar13 << 0x10;
        }
        if (uVar5 != uVar13) {
          uVar13 = (uVar5 & 0xff00ff00) >> 8 | (uVar5 & 0xff00ff) << 8;
          uVar13 = uVar13 >> 0x10 | uVar13 << 0x10;
          local_64 = uVar13;
          FUN_ffff00009076e6d4(param_1,iVar4,s_max_brightness_ffff0000907dc635,&local_64,4);
          local_64 = uVar13;
          FUN_ffff00009076e6d4(param_1,iVar4,s_max_hw_brightness_ffff0000907e2a5e,&local_64,4);
        }
        iVar6 = FUN_ffff00009075e200(iVar2);
        if ((iVar6 == 0) && (uVar12 == 4)) {
          local_64 = 0x6000000;
          FUN_ffff00009076e6d4(param_1,iVar4,s_led_mode_ffff0000907feecf,&local_64,4);
          iVar6 = 0;
        }
        else if ((iVar6 == 1) && (uVar12 != 4)) {
          local_64 = 0x4000000;
          FUN_ffff00009076e6d4(param_1,iVar4,s_led_mode_ffff0000907feecf,&local_64,4);
          iVar6 = 1;
        }
      }
      puVar9 = (undefined1 *)FUN_ffff00009077f4bc(&local_a8,0x2f);
      if (puVar9 != (undefined1 *)0x0) {
        *puVar9 = 0;
      }
      iVar4 = FUN_ffff00009076da24(param_1,&local_a8);
      if ((0 < iVar4) &&
         (lVar10 = FUN_ffff00009076df9c(param_1,iVar4,s_compatible_ffff0000907d74bf,&local_68),
         lVar10 != 0)) {
        bVar11 = (bool)(6 < uVar15 | bVar11);
        if ((iVar6 == 0) && (uVar12 == 4)) {
          pcVar14 = s_mediatek_i2c_leds_ffff0000907e46bd;
        }
        else {
          if (iVar6 != 1) goto LAB_ffff00009072a7d8;
          if (!bVar11) {
            iVar2 = FUN_ffff00009077f250(s_mediatek_disp_leds_ffff0000907ee009);
            FUN_ffff00009076e6d4
                      (param_1,iVar4,s_compatible_ffff0000907d74bf,
                       s_mediatek_disp_leds_ffff0000907ee009,iVar2 + 1);
            return;
          }
          bVar11 = true;
          pcVar14 = s_mediatek_disp_conn_leds_ffff00009080240e;
        }
        iVar3 = FUN_ffff00009077f250(pcVar14);
        FUN_ffff00009076e6d4(param_1,iVar4,s_compatible_ffff0000907d74bf,pcVar14,iVar3 + 1);
      }
LAB_ffff00009072a7d8:
      iVar2 = iVar2 + -1;
      bVar1 = 6 < uVar15;
      uVar15 = uVar15 - 1;
    } while (bVar1);
  }
  return;
}


