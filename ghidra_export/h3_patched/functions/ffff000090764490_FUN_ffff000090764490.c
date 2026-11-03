// ffff000090764490  size=1620  FUN_ffff000090764490

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090764490(void)

{
  bool bVar1;
  int iVar2;
  ulong uVar3;
  long lVar4;
  ulong uVar5;
  undefined8 uVar6;
  undefined8 uVar7;
  undefined8 uVar8;
  
  uVar3 = FUN_ffff000090760c7c(s_blackbox_ffff0000907f0c04,&DAT_ffff00009087cb50,0x400,0x3c);
  if ((uVar3 & 1) == 0) {
    FUN_ffff00009077de54
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa517,
               s_CheckBlackboxLogTableHeader_ffff0000907e5174,0x4a5,s_BLACKBOX_ffff0000907d9988);
    uVar6 = 0x829;
    uVar7 = 0x829;
    uVar8 = 0;
  }
  else {
    if (_DAT_ffff00009087cb50 != 0x56775af41bcde0f3) {
      FUN_ffff00009077de54
                (s__s__d____s___init_log_table_head_ffff0000907e9c13,
                 s_CheckBlackboxLogTableHeader_ffff0000907e5174,0x4ab,s_BLACKBOX_ffff0000907d9988);
      FUN_ffff0000907609d4(&DAT_ffff00009087cb50);
    }
    FUN_ffff0000907612e8(&DAT_ffff00009087cb50);
    iVar2 = FUN_ffff00009071c390();
    if (iVar2 == 0x1e) {
      FUN_ffff00009077de54
                (s__s__d____s___BR_POWER_EXC_type__s_ffff0000907d4fd0,
                 s_WritePLLKHardresetLog_ffff0000907e9c37,0x82f,s_BLACKBOX_ffff0000907d9988);
      _DAT_ffff00009087cb6c = 1;
    }
    uVar3 = FUN_ffff0000907626b4();
    if ((uVar3 & 1) == 0) {
      uVar6 = 0x836;
      uVar7 = 0x836;
      uVar8 = 1;
    }
    else {
      uVar3 = FUN_ffff000090762820();
      if ((uVar3 & 1) != 0) {
        if ((_DAT_ffff00009087cba4 == 0) || (_DAT_ffff00009087cbb0 != -0x4a3b2d5a16c92487)) {
          bVar1 = false;
        }
        else {
          FUN_ffff00009077de54
                    (s__s__d____s___identify_aee_hardre_ffff0000907e51b6,
                     s_WritePLLKHardresetLog_ffff0000907e9c37,0x842,s_BLACKBOX_ffff0000907d9988);
          bVar1 = 0 < _DAT_ffff00009087cb60 && _DAT_ffff00009087cbe0 != 0;
        }
        FUN_ffff00009077de54
                  (s__s__d____s___boot_index____d_ffff0000907d1abb,
                   s_WritePLLKHardresetLog_ffff0000907e9c37,0x847,s_BLACKBOX_ffff0000907d9988,
                   _DAT_ffff00009087cb60);
        if ((bVar1) && (_DAT_ffff00009087cbe0 != 2)) {
          uVar3 = 0;
          _DAT_ffff00009087cbe0 = 2;
        }
        else {
          _DAT_ffff00009087cb60 = _DAT_ffff00009087cb60 + 1;
          iVar2 = 1;
          if (_DAT_ffff00009087cba4 != 5) {
            iVar2 = _DAT_ffff00009087cba4 + 1;
          }
          _DAT_ffff00009087cba4 = iVar2;
          uVar3 = FUN_ffff000090762820();
          if ((uVar3 & 1) == 0) {
            uVar6 = 0x851;
            uVar7 = 0x851;
            goto LAB_ffff0000907648c0;
          }
          uVar3 = (ulong)(uint)(_DAT_ffff00009087cba0 +
                               (_DAT_ffff00009087cba4 + -1) * _DAT_ffff00009087cba8);
          _DAT_ffff00009087cbe0 = 0;
          _DAT_ffff00009087cbc4 = _DAT_ffff00009087cb60;
        }
        lVar4 = FUN_ffff00009077d24c(_DAT_ffff00009087cbcc);
        if (lVar4 == 0) {
          FUN_ffff00009077de54
                    (s__s__d____s____d__malloc_failed__ffff0000907dd040,
                     s_WritePLLKHardresetLog_ffff0000907e9c37,0x85e,s_BLACKBOX_ffff0000907d9988,
                     0x85e);
        }
        else {
          FUN_ffff00009077eb88(lVar4,0,_DAT_ffff00009087cbcc);
          uVar5 = FUN_ffff000090760d38
                            (s_blackbox_ffff0000907f0c04,lVar4,uVar3 + _DAT_ffff00009087cb9c,
                             _DAT_ffff00009087cbcc);
          if ((uVar5 & 1) == 0) {
            FUN_ffff00009077de54
                      (s__s__d____s____d__erase_warmreset_ffff0000907e6975,
                       s_WritePLLKHardresetLog_ffff0000907e9c37,0x865,s_BLACKBOX_ffff0000907d9988,
                       0x865);
          }
          else {
            if ((DAT_ffff00009087bf14 & 0xfe) == 2) {
              uVar5 = FUN_ffff0000907629c0();
              if ((uVar5 & 1) == 0) {
                FUN_ffff00009077de54
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000907f343b,
                           s_WritePLLKHardresetLog_ffff0000907e9c37,0x86e,
                           s_BLACKBOX_ffff0000907d9988,0x86e,10);
                goto LAB_ffff0000907648cc;
              }
              uVar5 = FUN_ffff000090762b10();
              if ((uVar5 & 1) == 0) {
                FUN_ffff00009077de54
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000907f343b,
                           s_WritePLLKHardresetLog_ffff0000907e9c37,0x874,
                           s_BLACKBOX_ffff0000907d9988,0x874,0xb);
                goto LAB_ffff0000907648cc;
              }
              uVar5 = FUN_ffff000090762c60();
              if ((uVar5 & 1) == 0) {
                FUN_ffff00009077de54
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000907f343b,
                           s_WritePLLKHardresetLog_ffff0000907e9c37,0x87a,
                           s_BLACKBOX_ffff0000907d9988,0x87a,0xc);
                goto LAB_ffff0000907648cc;
              }
              uVar5 = FUN_ffff000090762e20();
              if ((uVar5 & 1) == 0) {
                FUN_ffff00009077de54
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000907f343b,
                           s_WritePLLKHardresetLog_ffff0000907e9c37,0x881,
                           s_BLACKBOX_ffff0000907d9988,0x881,0xd);
                goto LAB_ffff0000907648cc;
              }
              uVar5 = FUN_ffff000090762f0c();
              if ((uVar5 & 1) == 0) {
                FUN_ffff00009077de54
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000907f343b,
                           s_WritePLLKHardresetLog_ffff0000907e9c37,0x888,
                           s_BLACKBOX_ffff0000907d9988,0x888,0xe);
                goto LAB_ffff0000907648cc;
              }
              uVar5 = FUN_ffff000090762ff8();
              if ((uVar5 & 1) == 0) {
                FUN_ffff00009077de54
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000907f343b,
                           s_WritePLLKHardresetLog_ffff0000907e9c37,0x88e,
                           s_BLACKBOX_ffff0000907d9988,0x88e,0xf);
                goto LAB_ffff0000907648cc;
              }
              uVar5 = FUN_ffff0000907630e4();
              if ((uVar5 & 1) == 0) {
                FUN_ffff00009077de54
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000907f343b,
                           s_WritePLLKHardresetLog_ffff0000907e9c37,0x894,
                           s_BLACKBOX_ffff0000907d9988,0x894,0x10);
                goto LAB_ffff0000907648cc;
              }
            }
            if ((DAT_ffff00009087bf14 == 3) && (uVar5 = FUN_ffff0000907631f8(), (uVar5 & 1) == 0)) {
              FUN_ffff00009077de54
                        (s__s__d____s____d__CheckBlackboxHe_ffff0000907f343b,
                         s_WritePLLKHardresetLog_ffff0000907e9c37,0x89c,s_BLACKBOX_ffff0000907d9988,
                         0x89c,0x11);
            }
            else {
              uVar5 = FUN_ffff000090760d38
                                (s_blackbox_ffff0000907f0c04,&DAT_ffff00009087cb50,0x400,0x3c);
              if ((uVar5 & 1) == 0) {
                FUN_ffff00009077de54
                          (s__s__d____s____d__write_aee_log_t_ffff0000907f346c,
                           s_WritePLLKHardresetLog_ffff0000907e9c37,0x8a4,
                           s_BLACKBOX_ffff0000907d9988,0x8a4);
              }
              else {
                uVar5 = FUN_ffff000090760d38
                                  (s_blackbox_ffff0000907f0c04,&DAT_ffff00009087cb90,
                                   _DAT_ffff00009087cb64,0x1c);
                if ((uVar5 & 1) == 0) {
                  FUN_ffff00009077de54
                            (s__s__d____s____d__write_aee_log_c_ffff0000907d8297,
                             s_WritePLLKHardresetLog_ffff0000907e9c37,0x8a9,
                             s_BLACKBOX_ffff0000907d9988,0x8a9);
                }
                else {
                  uVar5 = FUN_ffff000090760d38
                                    (s_blackbox_ffff0000907f0c04,&DAT_ffff00009087cbb0,uVar3,0x80);
                  if ((uVar5 & 1) == 0) {
                    FUN_ffff00009077de54
                              (s__s__d____s____d__write_aee_index_ffff0000907f8a8b,
                               s_WritePLLKHardresetLog_ffff0000907e9c37,0x8ae,
                               s_BLACKBOX_ffff0000907d9988,0x8ae);
                  }
                  else {
                    if (DAT_ffff00009087bf14 != 1) {
                      FUN_ffff00009076242c();
                    }
                    FUN_ffff00009077de54
                              (s__s__d____s___GetPLLKlog_from_log_ffff0000907d1ad9,
                               s_WritePLLKHardresetLog_ffff0000907e9c37,0x8b7,
                               s_BLACKBOX_ffff0000907d9988);
                    uVar6 = FUN_ffff000090762374(uVar3 + _DAT_ffff00009087cbbc);
                    FUN_ffff00009077de54
                              (s__s__d____s___GetPLLKlog_from_log_ffff0000907e00b5,
                               s_WritePLLKHardresetLog_ffff0000907e9c37,0x8b9,
                               s_BLACKBOX_ffff0000907d9988,uVar6);
                  }
                }
              }
            }
          }
        }
        goto LAB_ffff0000907648cc;
      }
      uVar6 = 0x83e;
      uVar7 = 0x83e;
LAB_ffff0000907648c0:
      uVar8 = 2;
    }
  }
  FUN_ffff00009077de54
            (s__s__d____s____d__CheckBlackboxHe_ffff0000907f343b,
             s_WritePLLKHardresetLog_ffff0000907e9c37,uVar6,s_BLACKBOX_ffff0000907d9988,uVar7,uVar8)
  ;
  lVar4 = 0;
LAB_ffff0000907648cc:
  thunk_FUN_ffff0000907c2ad8(lVar4);
  return;
}


