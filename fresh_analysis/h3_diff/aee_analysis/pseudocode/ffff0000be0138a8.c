
undefined8 FUN_ffff0000be0138a8(undefined8 param_1,undefined4 param_2)

{
  ulong uVar1;
  int iVar2;
  int iVar3;
  long lVar4;
  undefined8 uVar5;
  long lVar6;
  code *pcVar7;
  undefined8 uVar8;
  undefined **ppuVar9;
  uint uVar10;
  undefined8 *puVar11;
  undefined **ppuVar12;
  char *pcVar13;
  int local_68;
  int local_64;
  
  FUN_ffff0000be05fd98
            (s__s__d___s___ffff0000be0c3cb9,s_fastboot_init_ffff0000be0b74c1,0x1b6,
             s_fastboot_init_ffff0000be0b74c1);
  DAT_ffff0000be107968 = param_1;
  DAT_ffff0000be107970 = param_2;
  iVar2 = FUN_ffff0000be01d36c();
  if (0 < iVar2) {
    DAT_ffff0000be107974 = iVar2;
    FUN_ffff0000be01b688(&DAT_ffff0000be107978);
    FUN_ffff0000be05fd98
              (s__s__d__Init__fastboot_idle_timer_ffff0000be0a7b32,s_fastboot_init_ffff0000be0b74c1,
               0x1bd,DAT_ffff0000be107974);
  }
  lVar4 = FUN_ffff0000be0133c0();
  if (lVar4 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98(s_app_fastboot_fastboot_c_ffff0000be0ab35b,0x1c1,&DAT_ffff0000be0ad432);
  }
  uVar5 = daif;
  uVar10 = (uint)uVar5;
  if ((uVar10 >> 7 & 1) == 0) {
    uVar1 = daif;
    daif = uVar1 | 0x80;
  }
  ppuVar9 = &PTR_LOOP_ffff0000be0d6058;
  DAT_ffff0000be0d6060 = 1;
  do {
    ppuVar12 = (undefined **)*ppuVar9;
    if (ppuVar12 == &PTR_LOOP_ffff0000be0d6050) {
      if ((uVar10 >> 7 & 1) == 0) {
        uVar1 = daif;
        daif = uVar1 & 0xffffffffffffff7f;
      }
      goto LAB_ffff0000be013f18;
    }
    puVar11 = (undefined8 *)ppuVar12[2];
    ppuVar9 = ppuVar12 + 1;
    uVar8 = *puVar11;
    uVar5 = FUN_ffff0000be060e5c(uVar8);
    iVar2 = FUN_ffff0000be060f54(uVar8,lVar4,uVar5);
  } while (iVar2 != 0);
  DAT_ffff0000be0d6060 = 0;
  if ((uVar10 >> 7 & 1) == 0) {
    uVar1 = daif;
    daif = uVar1 & 0xffffffffffffff7f;
    puVar11 = (undefined8 *)ppuVar12[2];
    if (puVar11 == (undefined8 *)0x0) {
LAB_ffff0000be013f18:
      DAT_ffff0000be107960 = (undefined8 *)0x0;
      DAT_ffff0000be0d6060 = 0;
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee98
                (s_app_fastboot_fastboot_c_ffff0000be0ab35b,0x1c4,s_interface_ffff0000be0ac5d0);
    }
  }
  DAT_ffff0000be107960 = puVar11;
  iVar2 = (*(code *)puVar11[1])();
  if (iVar2 < 0) {
    uVar5 = 0xffffffff;
  }
  else {
    (*(code *)DAT_ffff0000be107960[3])();
    FUN_ffff0000be01b688(&DAT_ffff0000be1079b0);
    do {
      FUN_ffff0000be01b830(&DAT_ffff0000be1079b0,900000,FUN_ffff0000be013864,0);
      puVar11 = DAT_ffff0000be107960;
      uVar8 = *DAT_ffff0000be107960;
      uVar5 = FUN_ffff0000be060e5c(uVar8);
      iVar2 = FUN_ffff0000be060f54(uVar8,&DAT_ffff0000be0bce82,uVar5);
      if (iVar2 == 0) {
        (*(code *)puVar11[7])();
      }
      FUN_ffff0000be01b850(&DAT_ffff0000be1079b0);
      lVar4 = FUN_ffff0000be05f204(0x40,0x40);
      if (lVar4 == 0) {
        FUN_ffff0000be05fd98(s_fastboot__can_t_allocate_memory_ffff0000be0c4ee7);
        iVar2 = -5;
      }
      else {
        while (DAT_ffff0000be107948 - 5U < 0xfffffffe) {
          if (DAT_ffff0000be107974 != 0) {
            FUN_ffff0000be01b830(&DAT_ffff0000be107978,DAT_ffff0000be107974,FUN_ffff0000be013840,0);
          }
          FUN_ffff0000be05fd98(s_fastboot__waiting_commands_ffff0000be0c18bc);
          iVar2 = (*(code *)DAT_ffff0000be107960[5])(lVar4,0x40);
          uVar8 = *DAT_ffff0000be107960;
          uVar5 = FUN_ffff0000be060e5c(uVar8);
          iVar3 = FUN_ffff0000be060f54(uVar8,&DAT_ffff0000be0bce82,uVar5);
          if ((iVar3 == 0) && (iVar2 < 0)) break;
          iVar3 = FUN_ffff0000be060f54(uVar8,&DAT_ffff0000be0b74cf,uVar5);
          if ((iVar3 != 0) || (0 < iVar2)) {
            if (DAT_ffff0000be107974 != 0) {
              FUN_ffff0000be01b850(&DAT_ffff0000be107978);
            }
            *(undefined1 *)(lVar4 + iVar2) = 0;
            FUN_ffff0000be05fd98(s_fastboot___s_len__d__ffff0000be0b2c94,lVar4,iVar2);
            FUN_ffff0000be05fd98
                      (s_fastboot__download_base__p__down_ffff0000be0a7b69,DAT_ffff0000be107968,
                       DAT_ffff0000be1079a8);
            puVar11 = &DAT_ffff0000be107950;
            do {
              puVar11 = (undefined8 *)*puVar11;
              if (puVar11 == (undefined8 *)0x0) {
                FUN_ffff0000be05fd98(s__unknown_command____s___ffff0000be0b09c1,lVar4);
                if (DAT_ffff0000be107948 == 1) {
                  lVar6 = FUN_ffff0000be05f204(0x40,0x40);
                  if (lVar6 == 0) {
                    FUN_ffff0000be05fd98(s_fastboot__can_t_allocate_memory_ffff0000be0c4ee7);
                  }
                  else {
                    FUN_ffff0000be0611dc
                              (lVar6,0x40,&DAT_ffff0000be0af57f,&DAT_ffff0000be0aa1db,
                               s_unknown_command_ffff0000be0ac5da);
                    pcVar7 = (code *)DAT_ffff0000be107960[6];
                    DAT_ffff0000be107948 = 2;
                    uVar5 = FUN_ffff0000be060e5c(lVar6);
                    (*pcVar7)(lVar6,uVar5,5000);
                    thunk_FUN_ffff0000be09937c(lVar6);
                  }
                }
                pcVar13 = s_unknown_command_ffff0000be0ac5da;
                goto LAB_ffff0000be013dcc;
              }
              uVar5 = puVar11[1];
              uVar10 = *(uint *)(puVar11 + 2);
              DAT_ffff0000be107948 = 1;
              iVar2 = FUN_ffff0000be060778(lVar4,uVar5,(ulong)uVar10);
            } while (iVar2 != 0);
            FUN_ffff0000be05fd98
                      (s_fastboot__cmd__s___arg__s__ffff0000be0be056,uVar5,lVar4 + (ulong)uVar10);
            local_64 = 1;
            local_68 = 4;
            FUN_ffff0000be087d48(&local_64);
            FUN_ffff0000be0679e4(&local_68);
            if (((local_64 == 0) || (*(int *)((long)puVar11 + 0x14) != 0)) &&
               ((*(int *)(puVar11 + 3) == 0 || (local_68 == 3)))) {
              (*(code *)puVar11[4])
                        (lVar4 + (ulong)*(uint *)(puVar11 + 2),DAT_ffff0000be107968,
                         DAT_ffff0000be1079a8);
            }
            iVar2 = local_68;
            if (DAT_ffff0000be107948 == 1) {
              if ((local_64 == 1) && (*(int *)((long)puVar11 + 0x14) == 0)) {
                lVar6 = FUN_ffff0000be05f204(0x40,0x40);
                if (lVar6 != 0) {
                  pcVar13 = s_No_support_by_security_control_ffff0000be0b99b1;
                  goto LAB_ffff0000be013d88;
                }
                FUN_ffff0000be05fd98(s_fastboot__can_t_allocate_memory_ffff0000be0c4ee7);
                pcVar13 = s_No_support_by_security_control_ffff0000be0b99b1;
              }
              else {
                iVar3 = *(int *)(puVar11 + 3);
                lVar6 = FUN_ffff0000be05f204(0x40,0x40);
                if ((iVar3 == 0) || (iVar2 == 3)) {
                  if (lVar6 != 0) {
                    pcVar13 = s_unknown_reason_ffff0000be0bce86;
                    goto LAB_ffff0000be013d88;
                  }
                  FUN_ffff0000be05fd98(s_fastboot__can_t_allocate_memory_ffff0000be0c4ee7);
                  pcVar13 = s_unknown_reason_ffff0000be0bce86;
                }
                else if (lVar6 == 0) {
                  FUN_ffff0000be05fd98(s_fastboot__can_t_allocate_memory_ffff0000be0c4ee7);
                  pcVar13 = s_No_support_by_lock_control_ffff0000be0c04dc;
                }
                else {
                  pcVar13 = s_No_support_by_lock_control_ffff0000be0c04dc;
LAB_ffff0000be013d88:
                  FUN_ffff0000be0611dc
                            (lVar6,0x40,&DAT_ffff0000be0af57f,&DAT_ffff0000be0aa1db,pcVar13);
                  pcVar7 = (code *)DAT_ffff0000be107960[6];
                  DAT_ffff0000be107948 = 2;
                  uVar5 = FUN_ffff0000be060e5c(lVar6);
                  (*pcVar7)(lVar6,uVar5,5000);
                  thunk_FUN_ffff0000be09937c(lVar6);
                }
              }
LAB_ffff0000be013dcc:
              FUN_ffff0000be05fd98(&DAT_ffff0000be0ba9be,pcVar13);
            }
          }
        }
        if (DAT_ffff0000be107948 != 4) {
          DAT_ffff0000be107948 = 0;
        }
        FUN_ffff0000be05fd98(s_fastboot__oops__ffff0000be0a6a56);
        thunk_FUN_ffff0000be09937c(lVar4);
        iVar2 = DAT_ffff0000be107948;
      }
    } while (iVar2 != 4);
    (*(code *)DAT_ffff0000be107960[4])();
    (*(code *)DAT_ffff0000be107960[2])();
    uVar5 = daif;
    if (((uint)uVar5 >> 7 & 1) == 0) {
      uVar1 = daif;
      daif = uVar1 | 0x80;
    }
    DAT_ffff0000be0d6060 = 1;
    ppuVar9 = (undefined **)PTR_LOOP_ffff0000be0d6058;
    while (ppuVar9 != &PTR_LOOP_ffff0000be0d6050) {
      ppuVar9 = (undefined **)ppuVar9[1];
      thunk_FUN_ffff0000be09937c();
    }
    DAT_ffff0000be0d6060 = 0;
    if (((uint)uVar5 >> 7 & 1) == 0) {
      uVar1 = daif;
      daif = uVar1 & 0xffffffffffffff7f;
    }
    uVar5 = 0;
  }
  return uVar5;
}


