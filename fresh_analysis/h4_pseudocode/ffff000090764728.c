
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090764728(void)

{
  bool bVar1;
  int iVar2;
  ulong uVar3;
  long lVar4;
  ulong uVar5;
  undefined8 uVar6;
  undefined8 uVar7;
  undefined8 uVar8;
  
  uVar3 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087cbe8,0x400,0x3c);
  if ((uVar3 & 1) == 0) {
    FUN_ffff00009077e11c
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa6b3,
               s_CheckBlackboxLogTableHeader_ffff0000907e528f,0x4a5,s_BLACKBOX_ffff0000907d9a1f);
    uVar6 = 0x829;
    uVar7 = 0x829;
    uVar8 = 0;
  }
  else {
    if (DAT_ffff00009087cbe8 != 0x56775af41bcde0f3) {
      FUN_ffff00009077e11c
                (s__s__d____s___init_log_table_head_ffff0000907e9d6e,
                 s_CheckBlackboxLogTableHeader_ffff0000907e528f,0x4ab,s_BLACKBOX_ffff0000907d9a1f);
      FUN_ffff000090760c6c(&DAT_ffff00009087cbe8);
    }
    FUN_ffff000090761580(&DAT_ffff00009087cbe8);
    iVar2 = FUN_ffff00009071c4d4();
    if (iVar2 == 0x1e) {
      FUN_ffff00009077e11c
                (s__s__d____s___BR_POWER_EXC_type__s_ffff0000907d500d,
                 s_WritePLLKHardresetLog_ffff0000907e9d92,0x82f,s_BLACKBOX_ffff0000907d9a1f);
      DAT_ffff00009087cc04 = 1;
    }
    uVar3 = FUN_ffff00009076294c();
    if ((uVar3 & 1) == 0) {
      uVar6 = 0x836;
      uVar7 = 0x836;
      uVar8 = 1;
    }
    else {
      uVar3 = FUN_ffff000090762ab8();
      if ((uVar3 & 1) != 0) {
        if ((DAT_ffff00009087cc3c == 0) || (DAT_ffff00009087cc48 != -0x4a3b2d5a16c92487)) {
          bVar1 = false;
        }
        else {
          FUN_ffff00009077e11c
                    (s__s__d____s___identify_aee_hardre_ffff0000907e52d1,
                     s_WritePLLKHardresetLog_ffff0000907e9d92,0x842,s_BLACKBOX_ffff0000907d9a1f);
          bVar1 = 0 < DAT_ffff00009087cbf8 && DAT_ffff00009087cc78 != 0;
        }
        FUN_ffff00009077e11c
                  (s__s__d____s___boot_index____d_ffff0000907d1af8,
                   s_WritePLLKHardresetLog_ffff0000907e9d92,0x847,s_BLACKBOX_ffff0000907d9a1f,
                   DAT_ffff00009087cbf8);
        if ((bVar1) && (DAT_ffff00009087cc78 != 2)) {
          uVar3 = 0;
          DAT_ffff00009087cc78 = 2;
        }
        else {
          DAT_ffff00009087cbf8 = DAT_ffff00009087cbf8 + 1;
          iVar2 = 1;
          if (DAT_ffff00009087cc3c != 5) {
            iVar2 = DAT_ffff00009087cc3c + 1;
          }
          DAT_ffff00009087cc3c = iVar2;
          uVar3 = FUN_ffff000090762ab8();
          if ((uVar3 & 1) == 0) {
            uVar6 = 0x851;
            uVar7 = 0x851;
            goto LAB_ffff000090764b58;
          }
          uVar3 = (ulong)(uint)(DAT_ffff00009087cc38 +
                               (DAT_ffff00009087cc3c + -1) * DAT_ffff00009087cc40);
          DAT_ffff00009087cc78 = 0;
          DAT_ffff00009087cc5c = DAT_ffff00009087cbf8;
        }
        lVar4 = FUN_ffff00009077d514(DAT_ffff00009087cc60._4_4_);
        if (lVar4 == 0) {
          FUN_ffff00009077e11c
                    (s__s__d____s____d__malloc_failed__ffff0000907dd12d,
                     s_WritePLLKHardresetLog_ffff0000907e9d92,0x85e,s_BLACKBOX_ffff0000907d9a1f,
                     0x85e);
        }
        else {
          FUN_ffff00009077ee50(lVar4,0,DAT_ffff00009087cc60._4_4_);
          uVar5 = FUN_ffff000090760fd0
                            (s_blackbox_ffff0000907f0d8c,lVar4,uVar3 + _DAT_ffff00009087cc34,
                             DAT_ffff00009087cc60._4_4_);
          if ((uVar5 & 1) == 0) {
            FUN_ffff00009077e11c
                      (s__s__d____s____d__erase_warmreset_ffff0000907e6aa0,
                       s_WritePLLKHardresetLog_ffff0000907e9d92,0x865,s_BLACKBOX_ffff0000907d9a1f,
                       0x865);
          }
          else {
            if ((DAT_ffff00009087bfac & 0xfe) == 2) {
              uVar5 = FUN_ffff000090762c58();
              if ((uVar5 & 1) == 0) {
                FUN_ffff00009077e11c
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000907f35de,
                           s_WritePLLKHardresetLog_ffff0000907e9d92,0x86e,
                           s_BLACKBOX_ffff0000907d9a1f,0x86e,10);
                goto LAB_ffff000090764b64;
              }
              uVar5 = FUN_ffff000090762da8();
              if ((uVar5 & 1) == 0) {
                FUN_ffff00009077e11c
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000907f35de,
                           s_WritePLLKHardresetLog_ffff0000907e9d92,0x874,
                           s_BLACKBOX_ffff0000907d9a1f,0x874,0xb);
                goto LAB_ffff000090764b64;
              }
              uVar5 = FUN_ffff000090762ef8();
              if ((uVar5 & 1) == 0) {
                FUN_ffff00009077e11c
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000907f35de,
                           s_WritePLLKHardresetLog_ffff0000907e9d92,0x87a,
                           s_BLACKBOX_ffff0000907d9a1f,0x87a,0xc);
                goto LAB_ffff000090764b64;
              }
              uVar5 = FUN_ffff0000907630b8();
              if ((uVar5 & 1) == 0) {
                FUN_ffff00009077e11c
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000907f35de,
                           s_WritePLLKHardresetLog_ffff0000907e9d92,0x881,
                           s_BLACKBOX_ffff0000907d9a1f,0x881,0xd);
                goto LAB_ffff000090764b64;
              }
              uVar5 = FUN_ffff0000907631a4();
              if ((uVar5 & 1) == 0) {
                FUN_ffff00009077e11c
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000907f35de,
                           s_WritePLLKHardresetLog_ffff0000907e9d92,0x888,
                           s_BLACKBOX_ffff0000907d9a1f,0x888,0xe);
                goto LAB_ffff000090764b64;
              }
              uVar5 = FUN_ffff000090763290();
              if ((uVar5 & 1) == 0) {
                FUN_ffff00009077e11c
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000907f35de,
                           s_WritePLLKHardresetLog_ffff0000907e9d92,0x88e,
                           s_BLACKBOX_ffff0000907d9a1f,0x88e,0xf);
                goto LAB_ffff000090764b64;
              }
              uVar5 = FUN_ffff00009076337c();
              if ((uVar5 & 1) == 0) {
                FUN_ffff00009077e11c
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000907f35de,
                           s_WritePLLKHardresetLog_ffff0000907e9d92,0x894,
                           s_BLACKBOX_ffff0000907d9a1f,0x894,0x10);
                goto LAB_ffff000090764b64;
              }
            }
            if ((DAT_ffff00009087bfac == 3) && (uVar5 = FUN_ffff000090763490(), (uVar5 & 1) == 0)) {
              FUN_ffff00009077e11c
                        (s__s__d____s____d__CheckBlackboxHe_ffff0000907f35de,
                         s_WritePLLKHardresetLog_ffff0000907e9d92,0x89c,s_BLACKBOX_ffff0000907d9a1f,
                         0x89c,0x11);
            }
            else {
              uVar5 = FUN_ffff000090760fd0
                                (s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087cbe8,0x400,0x3c);
              if ((uVar5 & 1) == 0) {
                FUN_ffff00009077e11c
                          (s__s__d____s____d__write_aee_log_t_ffff0000907f360f,
                           s_WritePLLKHardresetLog_ffff0000907e9d92,0x8a4,
                           s_BLACKBOX_ffff0000907d9a1f,0x8a4);
              }
              else {
                uVar5 = FUN_ffff000090760fd0
                                  (s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087cc28,
                                   DAT_ffff00009087cbfc,0x1c);
                if ((uVar5 & 1) == 0) {
                  FUN_ffff00009077e11c
                            (s__s__d____s____d__write_aee_log_c_ffff0000907d832d,
                             s_WritePLLKHardresetLog_ffff0000907e9d92,0x8a9,
                             s_BLACKBOX_ffff0000907d9a1f,0x8a9);
                }
                else {
                  uVar5 = FUN_ffff000090760fd0
                                    (s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087cc48,uVar3,0x80);
                  if ((uVar5 & 1) == 0) {
                    FUN_ffff00009077e11c
                              (s__s__d____s____d__write_aee_index_ffff0000907f8c27,
                               s_WritePLLKHardresetLog_ffff0000907e9d92,0x8ae,
                               s_BLACKBOX_ffff0000907d9a1f,0x8ae);
                  }
                  else {
                    if (DAT_ffff00009087bfac != 1) {
                      FUN_ffff0000907626c4();
                    }
                    FUN_ffff00009077e11c
                              (s__s__d____s___GetPLLKlog_from_log_ffff0000907d1b16,
                               s_WritePLLKHardresetLog_ffff0000907e9d92,0x8b7,
                               s_BLACKBOX_ffff0000907d9a1f);
                    uVar6 = FUN_ffff00009076260c(uVar3 + DAT_ffff00009087cc50._4_4_);
                    FUN_ffff00009077e11c
                              (s__s__d____s___GetPLLKlog_from_log_ffff0000907e016f,
                               s_WritePLLKHardresetLog_ffff0000907e9d92,0x8b9,
                               s_BLACKBOX_ffff0000907d9a1f,uVar6);
                  }
                }
              }
            }
          }
        }
        goto LAB_ffff000090764b64;
      }
      uVar6 = 0x83e;
      uVar7 = 0x83e;
LAB_ffff000090764b58:
      uVar8 = 2;
    }
  }
  FUN_ffff00009077e11c
            (s__s__d____s____d__CheckBlackboxHe_ffff0000907f35de,
             s_WritePLLKHardresetLog_ffff0000907e9d92,uVar6,s_BLACKBOX_ffff0000907d9a1f,uVar7,uVar8)
  ;
  lVar4 = 0;
LAB_ffff000090764b64:
  thunk_FUN_ffff0000907c314c(lVar4);
  return;
}


