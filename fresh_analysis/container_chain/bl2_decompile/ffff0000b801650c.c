
undefined4 FUN_ffff0000b801650c(void)

{
  char cVar1;
  long lVar2;
  byte bVar3;
  int iVar4;
  int iVar5;
  int iVar6;
  int iVar7;
  uint uVar8;
  char *pcVar9;
  undefined8 uVar10;
  ulong uVar11;
  ulong uVar12;
  undefined8 uVar13;
  undefined8 uVar14;
  long lVar15;
  long lVar16;
  undefined *puVar17;
  char *pcVar18;
  int iVar19;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  
  FUN_ffff0000b802c1b8(0);
  iVar5 = 0;
  do {
    iVar19 = iVar5;
    FUN_ffff0000b802be7c();
    FUN_ffff0000b802bc3c(0,0,0);
    FUN_ffff0000b802bd58();
    iVar4 = FUN_ffff0000b803fbc8();
    iVar5 = FUN_ffff0000b803fc74();
    if (iVar5 < 0) {
      iVar5 = iVar5 + 1;
    }
    iVar5 = iVar19 * 0x50 + (iVar5 >> 1);
    if ((DAT_ffff0000b811b0dc == 0) && ((DAT_ffff0000b811b0d8 & 1) == 0)) {
      if (*(char *)(DAT_ffff0000b811b0d0 + 0x10) != '\0') {
        uVar8 = 0;
        pcVar18 = (char *)(DAT_ffff0000b811b0d0 + 0x15);
        lVar15 = DAT_ffff0000b811b0d0;
        do {
          cVar1 = *pcVar18;
          while (cVar1 != '\n') {
            if ((cVar1 == ':') && (DAT_ffff0000b811b0dc != 2)) {
              if (pcVar18[2] == 'E') {
                pcVar9 = s_level_warning_flag____d__level_f_ffff0000b80924dd;
                uVar10 = 0;
                DAT_ffff0000b811b0d8 = 1;
                DAT_ffff0000b811b0dc = 2;
              }
              else if (pcVar18[2] == 'W') {
                pcVar9 = s_level_warning_flag____d__level_f_ffff0000b80924dd;
                uVar10 = 1;
                DAT_ffff0000b811b0dc = 2;
              }
              else {
                pcVar9 = s_no_level_warning_flag____d__leve_ffff0000b80942dc;
                uVar10 = 0;
                DAT_ffff0000b811b0d8 = 1;
              }
              FUN_ffff0000b804c33c(pcVar9,uVar10,DAT_ffff0000b811b0dc);
              iVar7 = DAT_ffff0000b811b0dc;
              bVar3 = DAT_ffff0000b811b0d8;
              if ((DAT_ffff0000b811b0dc == 2) || (DAT_ffff0000b811b0d8 != 0)) {
                pcVar9 = pcVar18 + 4;
                uVar10 = FUN_ffff0000b804d3cc(s_memory_ffff0000b80ae05c);
                iVar6 = FUN_ffff0000b804d4c4(s_memory_ffff0000b80ae05c,pcVar9,uVar10);
                if (iVar6 == 0) {
                  DAT_ffff0000b80ae058 = 0;
                  FUN_ffff0000b804c33c
                            (s_match_and_modulenum____d__level__ffff0000b80a01a6,0,iVar7,bVar3 ^ 1);
                }
                uVar10 = FUN_ffff0000b804d3cc(s_system_ffff0000b80ae066);
                iVar7 = FUN_ffff0000b804d4c4(s_system_ffff0000b80ae066,pcVar9,uVar10);
                if (iVar7 == 0) {
                  DAT_ffff0000b80ae058 = 1;
                  FUN_ffff0000b804c33c
                            (s_match_and_modulenum____d__level__ffff0000b80a01a6,1,
                             DAT_ffff0000b811b0dc,(DAT_ffff0000b811b0d8 ^ 0xff) & 1);
                }
                uVar10 = FUN_ffff0000b804d3cc(s_camera_ffff0000b80ae070);
                iVar7 = FUN_ffff0000b804d4c4(s_camera_ffff0000b80ae070,pcVar9,uVar10);
                if (iVar7 == 0) {
                  DAT_ffff0000b80ae058 = 2;
                  FUN_ffff0000b804c33c
                            (s_match_and_modulenum____d__level__ffff0000b80a01a6,2,
                             DAT_ffff0000b811b0dc,(DAT_ffff0000b811b0d8 ^ 0xff) & 1);
                }
                uVar10 = FUN_ffff0000b804d3cc(s_audio_ffff0000b80ae07a);
                iVar7 = FUN_ffff0000b804d4c4(s_audio_ffff0000b80ae07a,pcVar9,uVar10);
                if (iVar7 == 0) {
                  DAT_ffff0000b80ae058 = 3;
                  FUN_ffff0000b804c33c
                            (s_match_and_modulenum____d__level__ffff0000b80a01a6,3,
                             DAT_ffff0000b811b0dc,(DAT_ffff0000b811b0d8 ^ 0xff) & 1);
                }
                uVar10 = FUN_ffff0000b804d3cc(s_charger_ffff0000b80ae084);
                iVar7 = FUN_ffff0000b804d4c4(s_charger_ffff0000b80ae084,pcVar9,uVar10);
                if (iVar7 == 0) {
                  DAT_ffff0000b80ae058 = 4;
                  FUN_ffff0000b804c33c
                            (s_match_and_modulenum____d__level__ffff0000b80a01a6,4,
                             DAT_ffff0000b811b0dc,(DAT_ffff0000b811b0d8 ^ 0xff) & 1);
                }
                if ((DAT_ffff0000b811b0d8 & 1) != 0) goto LAB_ffff0000b8016858;
              }
            }
            pcVar18 = pcVar18 + 1;
            lVar15 = DAT_ffff0000b811b0d0;
            cVar1 = *pcVar18;
          }
          uVar8 = uVar8 + 1;
          pcVar18 = pcVar18 + 1;
          DAT_ffff0000b811b0dc = 1;
        } while (uVar8 < *(byte *)(lVar15 + 0x10));
      }
LAB_ffff0000b80168a4:
      FUN_ffff0000b804c33c(s_show_warning_ffff0000b808b644);
      iVar7 = iVar4 + -0x8b;
      if (0x8b < iVar4) {
        iVar7 = iVar4 + -0x8c;
      }
      FUN_ffff0000b802bdc4(&DAT_ffff0000b80cf6f0,iVar7 >> 1,iVar5,0x8c,0x46);
      pcVar18 = s_show_warning2_ffff0000b8087070;
      puVar17 = &DAT_ffff0000b80d9010;
    }
    else {
      FUN_ffff0000b804c33c
                (s_DisplayLevel_enter_not_first_lev_ffff0000b8091688,DAT_ffff0000b811b0dc,
                 DAT_ffff0000b80ae058);
      if (DAT_ffff0000b811b0d8 != 1) goto LAB_ffff0000b80168a4;
LAB_ffff0000b8016858:
      FUN_ffff0000b804c33c(s_show_fatal_error_ffff0000b809e34e);
      iVar7 = iVar4 + -0x103;
      if (0x103 < iVar4) {
        iVar7 = iVar4 + -0x104;
      }
      FUN_ffff0000b802bdc4(&DAT_ffff0000b80ae090,iVar7 >> 1,iVar5,0x104,0x46);
      pcVar18 = s_show_fatal_error2_ffff0000b8096164;
      puVar17 = (undefined *)0xffff0000b80bfcf0;
    }
    iVar7 = iVar4 + -399;
    if (399 < iVar4) {
      iVar7 = iVar4 + -400;
    }
    FUN_ffff0000b804c33c(pcVar18);
    FUN_ffff0000b802bdc4(puVar17,iVar7 >> 1,iVar5 + 0x50,400,0x28);
    local_70 = 0;
    uStack_68 = 0;
    local_80 = 0;
    uStack_78 = 0;
    local_90 = 0;
    uStack_88 = 0;
    local_a0 = 0;
    uStack_98 = 0;
    FUN_ffff0000b804d070(&local_a0,0,0x40);
    if ((DAT_ffff0000b811b0d8 & 1) == 0) {
      uVar10 = 0xf1;
      uVar13 = 0x7d;
      uVar14 = 0x30;
    }
    else {
      uVar10 = 0xff;
      uVar13 = 0;
      uVar14 = 0;
    }
    FUN_ffff0000b802bb48(uVar10,uVar13,uVar14);
    iVar5 = (iVar19 / 3) * 3;
    if (iVar19 == iVar5) {
      FUN_ffff0000b802bb04(3,0x32);
    }
    FUN_ffff0000b802b9d0(&DAT_ffff0000b808fa2f);
    FUN_ffff0000b802b9d0(s_<FATAL_ERROR_INFO>_<start>_ffff0000b809b626);
    if (*(char *)(DAT_ffff0000b811b0d0 + 0x10) != '\0') {
      uVar8 = 0;
      pcVar18 = (char *)(DAT_ffff0000b811b0d0 + 0x15);
      do {
        if (*pcVar18 == '\n') {
          lVar15 = 0;
          pcVar9 = pcVar18;
        }
        else {
          lVar16 = 0;
          do {
            lVar15 = lVar16 + 1;
            lVar2 = lVar16 + 1;
            lVar16 = lVar15;
          } while (pcVar18[lVar2] != '\n');
          pcVar9 = pcVar18 + lVar15;
        }
        FUN_ffff0000b804d428(&local_a0,pcVar18,(int)lVar15 + 1);
        FUN_ffff0000b804c33c(&DAT_ffff0000b8097cc8,&local_a0);
        FUN_ffff0000b802b9d0(s_ERROR_s_ffff0000b808707f,&local_a0);
        FUN_ffff0000b804d070(&local_a0,0,0x40);
        uVar8 = uVar8 + 1;
        pcVar18 = pcVar9 + 1;
      } while (uVar8 < *(byte *)(DAT_ffff0000b811b0d0 + 0x10));
    }
    FUN_ffff0000b802b9d0(s_<FATAL_ERROR_INFO>_<end>_ffff0000b8094309);
    FUN_ffff0000b802b9d0(s_Please_Press_Volumeup_more_than_3_ffff0000b808ea22);
    FUN_ffff0000b802b9d0(s_Please_Press_VolumeDown_more_tha_ffff0000b808b652);
    if (-1 < DAT_ffff0000b80ae058) {
      FUN_ffff0000b804c33c(s_will_enter_DisplayErrorModule_ffff0000b8097004);
      iVar7 = FUN_ffff0000b802bd30();
      iVar4 = iVar7 + 3;
      if (-1 < iVar7) {
        iVar4 = iVar7;
      }
      iVar7 = (iVar4 >> 2) * 3;
      iVar4 = FUN_ffff0000b802bd44();
      if (iVar4 < 0) {
        iVar4 = iVar4 + 1;
      }
      if (iVar19 == iVar5) {
        FUN_ffff0000b802bb04(iVar7,iVar4 >> 1);
      }
      uVar8 = DAT_ffff0000b80ae058 + 0x30U & 0xff;
      FUN_ffff0000b804c33c
                (s_DisplayErrorModule_enter_x__d_y__ffff0000b809e360,iVar7,iVar4 >> 1,
                 DAT_ffff0000b80ae058,uVar8);
      FUN_ffff0000b802b9d0(&DAT_ffff0000b808fa33,uVar8);
    }
    uVar11 = FUN_ffff0000b8009fb4();
    do {
      uVar8 = FUN_ffff0000b800eb5c(1,0);
      FUN_ffff0000b804c33c(s_temperature_in_lk2___d_ffff0000b808ea56,uVar8);
      if (0x39 < uVar8) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000b8014dbc(2,3);
      }
      uVar12 = FUN_ffff0000b801a544(6);
      if ((uVar12 & 1) == 0) {
        uVar12 = FUN_ffff0000b801a544(1);
        if ((uVar12 & 1) != 0) {
          FUN_ffff0000b804b704(3000000);
          uVar12 = FUN_ffff0000b801a544(1);
          if ((uVar12 & 1) != 0) {
            DAT_ffff0000b811b0c8 = 99;
            pcVar18 = s_Gsort_FACTORY_KEY_ffff0000b8099a94;
            goto LAB_ffff0000b8016bec;
          }
        }
      }
      else {
        FUN_ffff0000b804b704(3000000);
        uVar12 = FUN_ffff0000b801a544(6);
        if ((uVar12 & 1) != 0) {
          DAT_ffff0000b811b0c8 = 0;
          pcVar18 = s_Gsort_BOOT_MENU_KEY_ffff0000b8096177;
LAB_ffff0000b8016bec:
          FUN_ffff0000b804c33c(pcVar18);
          FUN_ffff0000b802bd58();
          thunk_FUN_ffff0000b8082558(DAT_ffff0000b811b0d0);
          return DAT_ffff0000b811b0c8;
        }
      }
      uVar12 = FUN_ffff0000b8009fb4();
      FUN_ffff0000b804c33c
                (s_end_tick__d_start_tick__d_ffff0000b8086135,uVar12 / 1000 & 0xffffffff,
                 uVar11 / 1000 & 0xffffffff);
    } while ((uint)((int)(uVar12 / 1000) - (int)(uVar11 / 1000)) < 300000000);
    iVar5 = 0;
    if (iVar19 < 2) {
      iVar5 = iVar19 + 1;
    }
  } while( true );
}


