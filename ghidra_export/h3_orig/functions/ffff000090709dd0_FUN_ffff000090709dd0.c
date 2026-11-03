// ffff000090709dd0  size=1672  FUN_ffff000090709dd0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090709dd0(undefined8 param_1,undefined4 param_2)

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
  
  FUN_ffff00009077de54
            (s__s__d___s___ffff0000907fe80a,s_fastboot_init_ffff0000907ebd21,0x1b6,
             s_fastboot_init_ffff0000907ebd21);
  _DAT_ffff00009083d178 = param_1;
  _DAT_ffff00009083d180 = param_2;
  iVar2 = FUN_ffff00009071c56c();
  if (0 < iVar2) {
    _DAT_ffff00009083d184 = iVar2;
    FUN_ffff000090718584(0xffff00009083d188);
    FUN_ffff00009077de54
              (s__s__d__Init__fastboot_idle_timer_ffff0000907d56c8,s_fastboot_init_ffff0000907ebd21,
               0x1bd,_DAT_ffff00009083d184);
  }
  lVar4 = FUN_ffff0000907098e8();
  if (lVar4 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04(s_app_fastboot_fastboot_c_ffff0000907da43a,0x1c1,&DAT_ffff0000907dd7d0);
  }
  uVar5 = daif;
  uVar10 = (uint)uVar5;
  if ((uVar10 >> 7 & 1) == 0) {
    uVar1 = daif;
    daif = uVar1 | 0x80;
  }
  ppuVar9 = &PTR_LOOP_ffff000090816c90;
  DAT_ffff000090816c98 = 1;
  do {
    ppuVar12 = (undefined **)*ppuVar9;
    if (ppuVar12 == &PTR_LOOP_ffff000090816c88) {
      if ((uVar10 >> 7 & 1) == 0) {
        uVar1 = daif;
        daif = uVar1 & 0xffffffffffffff7f;
      }
      goto LAB_ffff00009070a440;
    }
    puVar11 = (undefined8 *)ppuVar12[2];
    ppuVar9 = ppuVar12 + 1;
    uVar8 = *puVar11;
    uVar5 = FUN_ffff00009077ef88(uVar8);
    iVar2 = FUN_ffff00009077f080(uVar8,lVar4,uVar5);
  } while (iVar2 != 0);
  DAT_ffff000090816c98 = 0;
  if ((uVar10 >> 7 & 1) == 0) {
    uVar1 = daif;
    daif = uVar1 & 0xffffffffffffff7f;
    puVar11 = (undefined8 *)ppuVar12[2];
    if (puVar11 == (undefined8 *)0x0) {
LAB_ffff00009070a440:
      _DAT_ffff00009083d170 = (undefined8 *)0x0;
      DAT_ffff000090816c98 = 0;
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_app_fastboot_fastboot_c_ffff0000907da43a,0x1c4,s_interface_ffff0000907dbe15);
    }
  }
  _DAT_ffff00009083d170 = puVar11;
  iVar2 = (*(code *)puVar11[1])();
  if (iVar2 < 0) {
    uVar5 = 0xffffffff;
  }
  else {
    (*(code *)_DAT_ffff00009083d170[3])();
    FUN_ffff000090718584(0xffff00009083d1c0);
    do {
      FUN_ffff00009071872c(0xffff00009083d1c0,900000,FUN_ffff000090709d8c,0);
      puVar11 = _DAT_ffff00009083d170;
      uVar8 = *_DAT_ffff00009083d170;
      uVar5 = FUN_ffff00009077ef88(uVar8);
      iVar2 = FUN_ffff00009077f080(uVar8,&DAT_ffff0000907f3d69,uVar5);
      if (iVar2 == 0) {
        (*(code *)puVar11[7])();
      }
      FUN_ffff00009071874c(0xffff00009083d1c0);
      lVar4 = FUN_ffff00009077d294(0x40,0x40);
      if (lVar4 == 0) {
        FUN_ffff00009077de54(s_fastboot__can_t_allocate_memory_ffff0000908001d1);
        iVar2 = -5;
      }
      else {
        while (_DAT_ffff00009083d158 - 5U < 0xfffffffe) {
          if (_DAT_ffff00009083d184 != 0) {
            FUN_ffff00009071872c(0xffff00009083d188,_DAT_ffff00009083d184,FUN_ffff000090709d68,0);
          }
          FUN_ffff00009077de54(s_fastboot__waiting_commands_ffff0000907faed5);
          iVar2 = (*(code *)_DAT_ffff00009083d170[5])(lVar4,0x40);
          uVar8 = *_DAT_ffff00009083d170;
          uVar5 = FUN_ffff00009077ef88(uVar8);
          iVar3 = FUN_ffff00009077f080(uVar8,&DAT_ffff0000907f3d69,uVar5);
          if ((iVar3 == 0) && (iVar2 < 0)) break;
          iVar3 = FUN_ffff00009077f080(uVar8,&DAT_ffff0000907ebd2f,uVar5);
          if ((iVar3 != 0) || (0 < iVar2)) {
            if (_DAT_ffff00009083d184 != 0) {
              FUN_ffff00009071874c(0xffff00009083d188);
            }
            *(undefined1 *)(lVar4 + iVar2) = 0;
            FUN_ffff00009077de54(s_fastboot___s_len__d__ffff0000907e59f4,lVar4,iVar2);
            FUN_ffff00009077de54
                      (s_fastboot__download_base__p__down_ffff0000907d56ff,_DAT_ffff00009083d178,
                       _DAT_ffff00009083d1b8);
            puVar11 = (undefined8 *)&DAT_ffff00009083d160;
            do {
              puVar11 = (undefined8 *)*puVar11;
              if (puVar11 == (undefined8 *)0x0) {
                FUN_ffff00009077de54(s__unknown_command____s___ffff0000907e23cf,lVar4);
                if (_DAT_ffff00009083d158 == 1) {
                  lVar6 = FUN_ffff00009077d294(0x40,0x40);
                  if (lVar6 == 0) {
                    FUN_ffff00009077de54(s_fastboot__can_t_allocate_memory_ffff0000908001d1);
                  }
                  else {
                    FUN_ffff00009077f434
                              (lVar6,0x40,&DAT_ffff0000907e097c,&DAT_ffff0000907d8b8a,
                               s_unknown_command_ffff0000907dbe1f);
                    pcVar7 = (code *)_DAT_ffff00009083d170[6];
                    _DAT_ffff00009083d158 = 2;
                    uVar5 = FUN_ffff00009077ef88(lVar6);
                    (*pcVar7)(lVar6,uVar5,5000);
                    thunk_FUN_ffff0000907c2ad8(lVar6);
                  }
                }
                pcVar13 = s_unknown_command_ffff0000907dbe1f;
                goto LAB_ffff00009070a2f4;
              }
              uVar5 = puVar11[1];
              uVar10 = *(uint *)(puVar11 + 2);
              _DAT_ffff00009083d158 = 1;
              iVar2 = FUN_ffff00009077e820(lVar4,uVar5,(ulong)uVar10);
            } while (iVar2 != 0);
            FUN_ffff00009077de54
                      (s_fastboot__cmd__s___arg__s__ffff0000907f5ae2,uVar5,lVar4 + (ulong)uVar10);
            local_64 = 1;
            local_68 = 4;
            FUN_ffff0000907ad894(&local_64);
            FUN_ffff000090788750(&local_68);
            if (((local_64 == 0) || (*(int *)((long)puVar11 + 0x14) != 0)) &&
               ((*(int *)(puVar11 + 3) == 0 || (local_68 == 3)))) {
              (*(code *)puVar11[4])
                        (lVar4 + (ulong)*(uint *)(puVar11 + 2),_DAT_ffff00009083d178,
                         _DAT_ffff00009083d1b8);
            }
            iVar2 = local_68;
            if (_DAT_ffff00009083d158 == 1) {
              if ((local_64 == 1) && (*(int *)((long)puVar11 + 0x14) == 0)) {
                lVar6 = FUN_ffff00009077d294(0x40,0x40);
                if (lVar6 != 0) {
                  pcVar13 = s_No_support_by_security_control_ffff0000907ef22e;
                  goto LAB_ffff00009070a2b0;
                }
                FUN_ffff00009077de54(s_fastboot__can_t_allocate_memory_ffff0000908001d1);
                pcVar13 = s_No_support_by_security_control_ffff0000907ef22e;
              }
              else {
                iVar3 = *(int *)(puVar11 + 3);
                lVar6 = FUN_ffff00009077d294(0x40,0x40);
                if ((iVar3 == 0) || (iVar2 == 3)) {
                  if (lVar6 != 0) {
                    pcVar13 = s_unknown_reason_ffff0000907f3d6d;
                    goto LAB_ffff00009070a2b0;
                  }
                  FUN_ffff00009077de54(s_fastboot__can_t_allocate_memory_ffff0000908001d1);
                  pcVar13 = s_unknown_reason_ffff0000907f3d6d;
                }
                else if (lVar6 == 0) {
                  FUN_ffff00009077de54(s_fastboot__can_t_allocate_memory_ffff0000908001d1);
                  pcVar13 = s_No_support_by_lock_control_ffff0000907f9137;
                }
                else {
                  pcVar13 = s_No_support_by_lock_control_ffff0000907f9137;
LAB_ffff00009070a2b0:
                  FUN_ffff00009077f434
                            (lVar6,0x40,&DAT_ffff0000907e097c,&DAT_ffff0000907d8b8a,pcVar13);
                  pcVar7 = (code *)_DAT_ffff00009083d170[6];
                  _DAT_ffff00009083d158 = 2;
                  uVar5 = FUN_ffff00009077ef88(lVar6);
                  (*pcVar7)(lVar6,uVar5,5000);
                  thunk_FUN_ffff0000907c2ad8(lVar6);
                }
              }
LAB_ffff00009070a2f4:
              FUN_ffff00009077de54(&DAT_ffff0000907f0b34,pcVar13);
            }
          }
        }
        if (_DAT_ffff00009083d158 != 4) {
          _DAT_ffff00009083d158 = 0;
        }
        FUN_ffff00009077de54(s_fastboot__oops__ffff0000907d3dca);
        thunk_FUN_ffff0000907c2ad8(lVar4);
        iVar2 = _DAT_ffff00009083d158;
      }
    } while (iVar2 != 4);
    (*(code *)_DAT_ffff00009083d170[4])();
    (*(code *)_DAT_ffff00009083d170[2])();
    uVar5 = daif;
    if (((uint)uVar5 >> 7 & 1) == 0) {
      uVar1 = daif;
      daif = uVar1 | 0x80;
    }
    DAT_ffff000090816c98 = 1;
    ppuVar9 = (undefined **)PTR_LOOP_ffff000090816c90;
    while (ppuVar9 != &PTR_LOOP_ffff000090816c88) {
      ppuVar9 = (undefined **)ppuVar9[1];
      thunk_FUN_ffff0000907c2ad8();
    }
    DAT_ffff000090816c98 = 0;
    if (((uint)uVar5 >> 7 & 1) == 0) {
      uVar1 = daif;
      daif = uVar1 & 0xffffffffffffff7f;
    }
    uVar5 = 0;
  }
  return uVar5;
}


