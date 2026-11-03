// ffff000090709d5c  size=1672  FUN_ffff000090709d5c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090709d5c(undefined8 param_1,undefined4 param_2)

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
  
  FUN_ffff00009077e11c
            (s__s__d___s___ffff0000907fe9b2,s_fastboot_init_ffff0000907ebea4,0x1b6,
             s_fastboot_init_ffff0000907ebea4);
  _DAT_ffff00009083d118 = param_1;
  _DAT_ffff00009083d120 = param_2;
  iVar2 = FUN_ffff00009071c6f8();
  if (0 < iVar2) {
    _DAT_ffff00009083d124 = iVar2;
    FUN_ffff0000907186c8(0xffff00009083d128);
    FUN_ffff00009077e11c
              (s__s__d__Init__fastboot_idle_timer_ffff0000907d5705,s_fastboot_init_ffff0000907ebea4,
               0x1bd,_DAT_ffff00009083d124);
  }
  lVar4 = FUN_ffff000090709874();
  if (lVar4 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc(s_app_fastboot_fastboot_c_ffff0000907da4d1,0x1c1,&DAT_ffff0000907dd873);
  }
  uVar5 = daif;
  uVar10 = (uint)uVar5;
  if ((uVar10 >> 7 & 1) == 0) {
    uVar1 = daif;
    daif = uVar1 | 0x80;
  }
  ppuVar9 = &PTR_LOOP_ffff000090816ca0;
  DAT_ffff000090816ca8 = 1;
  do {
    ppuVar12 = (undefined **)*ppuVar9;
    if (ppuVar12 == &PTR_LOOP_ffff000090816c98) {
      if ((uVar10 >> 7 & 1) == 0) {
        uVar1 = daif;
        daif = uVar1 & 0xffffffffffffff7f;
      }
      goto LAB_ffff00009070a3cc;
    }
    puVar11 = (undefined8 *)ppuVar12[2];
    ppuVar9 = ppuVar12 + 1;
    uVar8 = *puVar11;
    uVar5 = FUN_ffff00009077f250(uVar8);
    iVar2 = FUN_ffff00009077f348(uVar8,lVar4,uVar5);
  } while (iVar2 != 0);
  DAT_ffff000090816ca8 = 0;
  if ((uVar10 >> 7 & 1) == 0) {
    uVar1 = daif;
    daif = uVar1 & 0xffffffffffffff7f;
    puVar11 = (undefined8 *)ppuVar12[2];
    if (puVar11 == (undefined8 *)0x0) {
LAB_ffff00009070a3cc:
      _DAT_ffff00009083d110 = (undefined8 *)0x0;
      DAT_ffff000090816ca8 = 0;
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_app_fastboot_fastboot_c_ffff0000907da4d1,0x1c4,s_interface_ffff0000907dbf20);
    }
  }
  _DAT_ffff00009083d110 = puVar11;
  iVar2 = (*(code *)puVar11[1])();
  if (iVar2 < 0) {
    uVar5 = 0xffffffff;
  }
  else {
    (*(code *)_DAT_ffff00009083d110[3])();
    FUN_ffff0000907186c8(0xffff00009083d160);
    do {
      FUN_ffff000090718870(0xffff00009083d160,900000,FUN_ffff000090709d18,0);
      puVar11 = _DAT_ffff00009083d110;
      uVar8 = *_DAT_ffff00009083d110;
      uVar5 = FUN_ffff00009077f250(uVar8);
      iVar2 = FUN_ffff00009077f348(uVar8,&DAT_ffff0000907f3ef5,uVar5);
      if (iVar2 == 0) {
        (*(code *)puVar11[7])();
      }
      FUN_ffff000090718890(0xffff00009083d160);
      lVar4 = FUN_ffff00009077d55c(0x40,0x40);
      if (lVar4 == 0) {
        FUN_ffff00009077e11c(s_fastboot__can_t_allocate_memory_ffff000090800379);
        iVar2 = -5;
      }
      else {
        while (_DAT_ffff00009083d0f8 - 5U < 0xfffffffe) {
          if (_DAT_ffff00009083d124 != 0) {
            FUN_ffff000090718870(0xffff00009083d128,_DAT_ffff00009083d124,FUN_ffff000090709cf4,0);
          }
          FUN_ffff00009077e11c(s_fastboot__waiting_commands_ffff0000907fb071);
          iVar2 = (*(code *)_DAT_ffff00009083d110[5])(lVar4,0x40);
          uVar8 = *_DAT_ffff00009083d110;
          uVar5 = FUN_ffff00009077f250(uVar8);
          iVar3 = FUN_ffff00009077f348(uVar8,&DAT_ffff0000907f3ef5,uVar5);
          if ((iVar3 == 0) && (iVar2 < 0)) break;
          iVar3 = FUN_ffff00009077f348(uVar8,&DAT_ffff0000907ebeb2,uVar5);
          if ((iVar3 != 0) || (0 < iVar2)) {
            if (_DAT_ffff00009083d124 != 0) {
              FUN_ffff000090718890(0xffff00009083d128);
            }
            *(undefined1 *)(lVar4 + iVar2) = 0;
            FUN_ffff00009077e11c(s_fastboot___s_len__d__ffff0000907e5b08,lVar4,iVar2);
            FUN_ffff00009077e11c
                      (s_fastboot__download_base__p__down_ffff0000907d573c,_DAT_ffff00009083d118,
                       _DAT_ffff00009083d158);
            puVar11 = (undefined8 *)&DAT_ffff00009083d100;
            do {
              puVar11 = (undefined8 *)*puVar11;
              if (puVar11 == (undefined8 *)0x0) {
                FUN_ffff00009077e11c(s__unknown_command____s___ffff0000907e24a0,lVar4);
                if (_DAT_ffff00009083d0f8 == 1) {
                  lVar6 = FUN_ffff00009077d55c(0x40,0x40);
                  if (lVar6 == 0) {
                    FUN_ffff00009077e11c(s_fastboot__can_t_allocate_memory_ffff000090800379);
                  }
                  else {
                    FUN_ffff00009077f6fc
                              (lVar6,0x40,&DAT_ffff0000907e0a1f,&DAT_ffff0000907d8c0b,
                               s_unknown_command_ffff0000907dbf2a);
                    pcVar7 = (code *)_DAT_ffff00009083d110[6];
                    _DAT_ffff00009083d0f8 = 2;
                    uVar5 = FUN_ffff00009077f250(lVar6);
                    (*pcVar7)(lVar6,uVar5,5000);
                    thunk_FUN_ffff0000907c314c(lVar6);
                  }
                }
                pcVar13 = s_unknown_command_ffff0000907dbf2a;
                goto LAB_ffff00009070a280;
              }
              uVar5 = puVar11[1];
              uVar10 = *(uint *)(puVar11 + 2);
              _DAT_ffff00009083d0f8 = 1;
              iVar2 = FUN_ffff00009077eae8(lVar4,uVar5,(ulong)uVar10);
            } while (iVar2 != 0);
            FUN_ffff00009077e11c
                      (s_fastboot__cmd__s___arg__s__ffff0000907f5c59,uVar5,lVar4 + (ulong)uVar10);
            local_64 = 1;
            local_68 = 4;
            FUN_ffff0000907adb54(&local_64);
            FUN_ffff000090788a10(&local_68);
            if (((local_64 == 0) || (*(int *)((long)puVar11 + 0x14) != 0)) &&
               ((*(int *)(puVar11 + 3) == 0 || (local_68 == 3)))) {
              (*(code *)puVar11[4])
                        (lVar4 + (ulong)*(uint *)(puVar11 + 2),_DAT_ffff00009083d118,
                         _DAT_ffff00009083d158);
            }
            iVar2 = local_68;
            if (_DAT_ffff00009083d0f8 == 1) {
              if ((local_64 == 1) && (*(int *)((long)puVar11 + 0x14) == 0)) {
                lVar6 = FUN_ffff00009077d55c(0x40,0x40);
                if (lVar6 != 0) {
                  pcVar13 = s_No_support_by_security_control_ffff0000907ef39f;
                  goto LAB_ffff00009070a23c;
                }
                FUN_ffff00009077e11c(s_fastboot__can_t_allocate_memory_ffff000090800379);
                pcVar13 = s_No_support_by_security_control_ffff0000907ef39f;
              }
              else {
                iVar3 = *(int *)(puVar11 + 3);
                lVar6 = FUN_ffff00009077d55c(0x40,0x40);
                if ((iVar3 == 0) || (iVar2 == 3)) {
                  if (lVar6 != 0) {
                    pcVar13 = s_unknown_reason_ffff0000907f3ef9;
                    goto LAB_ffff00009070a23c;
                  }
                  FUN_ffff00009077e11c(s_fastboot__can_t_allocate_memory_ffff000090800379);
                  pcVar13 = s_unknown_reason_ffff0000907f3ef9;
                }
                else if (lVar6 == 0) {
                  FUN_ffff00009077e11c(s_fastboot__can_t_allocate_memory_ffff000090800379);
                  pcVar13 = s_No_support_by_lock_control_ffff0000907f92d3;
                }
                else {
                  pcVar13 = s_No_support_by_lock_control_ffff0000907f92d3;
LAB_ffff00009070a23c:
                  FUN_ffff00009077f6fc
                            (lVar6,0x40,&DAT_ffff0000907e0a1f,&DAT_ffff0000907d8c0b,pcVar13);
                  pcVar7 = (code *)_DAT_ffff00009083d110[6];
                  _DAT_ffff00009083d0f8 = 2;
                  uVar5 = FUN_ffff00009077f250(lVar6);
                  (*pcVar7)(lVar6,uVar5,5000);
                  thunk_FUN_ffff0000907c314c(lVar6);
                }
              }
LAB_ffff00009070a280:
              FUN_ffff00009077e11c(&DAT_ffff0000907f0ca5,pcVar13);
            }
          }
        }
        if (_DAT_ffff00009083d0f8 != 4) {
          _DAT_ffff00009083d0f8 = 0;
        }
        FUN_ffff00009077e11c(s_fastboot__oops__ffff0000907d3e07);
        thunk_FUN_ffff0000907c314c(lVar4);
        iVar2 = _DAT_ffff00009083d0f8;
      }
    } while (iVar2 != 4);
    (*(code *)_DAT_ffff00009083d110[4])();
    (*(code *)_DAT_ffff00009083d110[2])();
    uVar5 = daif;
    if (((uint)uVar5 >> 7 & 1) == 0) {
      uVar1 = daif;
      daif = uVar1 | 0x80;
    }
    DAT_ffff000090816ca8 = 1;
    ppuVar9 = (undefined **)PTR_LOOP_ffff000090816ca0;
    while (ppuVar9 != &PTR_LOOP_ffff000090816c98) {
      ppuVar9 = (undefined **)ppuVar9[1];
      thunk_FUN_ffff0000907c314c();
    }
    DAT_ffff000090816ca8 = 0;
    if (((uint)uVar5 >> 7 & 1) == 0) {
      uVar1 = daif;
      daif = uVar1 & 0xffffffffffffff7f;
    }
    uVar5 = 0;
  }
  return uVar5;
}


