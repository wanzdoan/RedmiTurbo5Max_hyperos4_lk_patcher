
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be04f660(void)

{
  long lVar1;
  ulong uVar2;
  int iVar3;
  int iVar4;
  ulong uVar5;
  long lVar6;
  undefined8 uVar7;
  undefined8 uVar8;
  undefined8 uVar9;
  int iVar10;
  ulong uVar11;
  
  uVar5 = FUN_ffff0000be04cff4(s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126e20,0x400,0x3c);
  if ((uVar5 & 1) == 0) {
    FUN_ffff0000be05fd98
              (s__s__d____s___GetPartitionInfo_fa_ffff0000be0c10b3,
               s_CheckBlackboxLogTableHeader_ffff0000be0b24d1,0x4a5,s_BLACKBOX_ffff0000be0aa9ee);
    uVar7 = 0x78f;
    uVar8 = 0x78f;
    uVar9 = 0;
  }
  else {
    if (DAT_ffff0000be126e20 != 0x56775af41bcde0f3) {
      FUN_ffff0000be05fd98
                (s__s__d____s___init_log_table_head_ffff0000be0b5a43,
                 s_CheckBlackboxLogTableHeader_ffff0000be0b24d1,0x4ab,s_BLACKBOX_ffff0000be0aa9ee);
      FUN_ffff0000be04cebc(&DAT_ffff0000be126e20);
    }
    FUN_ffff0000be04d660(&DAT_ffff0000be126e20);
    iVar10 = 1;
    DAT_ffff0000be126e3c = 1;
    uVar5 = FUN_ffff0000be04ea2c();
    if ((uVar5 & 1) == 0) {
      uVar7 = 0x797;
      uVar8 = 0x797;
      uVar9 = 1;
    }
    else {
      DAT_ffff0000be126e30 = DAT_ffff0000be126e30 + 1;
      if (DAT_ffff0000be126e74 != 5) {
        iVar10 = DAT_ffff0000be126e74 + 1;
      }
      DAT_ffff0000be126e74 = iVar10;
      uVar5 = FUN_ffff0000be04eb98();
      iVar4 = DAT_ffff0000be126e78;
      iVar3 = DAT_ffff0000be126e74;
      iVar10 = DAT_ffff0000be126e70;
      if ((uVar5 & 1) != 0) {
        uVar2 = (ulong)DAT_ffff0000be126e98._4_4_;
        uVar5 = (ulong)_DAT_ffff0000be126e6c;
        DAT_ffff0000be126e94 = DAT_ffff0000be126e30;
        DAT_ffff0000be126eb0 = 1;
        lVar6 = FUN_ffff0000be05f1bc((undefined4)DAT_ffff0000be126e98);
        if (lVar6 == 0) {
          FUN_ffff0000be05fd98
                    (s__s__d____s___Alloc_warmreset_emp_ffff0000be0aedbd,
                     s_WritePLLKWarmresetLog_ffff0000be0a9766,0x7b3,s_BLACKBOX_ffff0000be0aa9ee);
        }
        else {
          uVar11 = (ulong)(uint)(iVar10 + (iVar3 + -1) * iVar4);
          lVar1 = uVar11 + uVar5 + uVar2;
          FUN_ffff0000be060ae0(lVar6,0,(undefined4)DAT_ffff0000be126e98);
          uVar5 = FUN_ffff0000be04d0b0
                            (s_blackbox_ffff0000be0bac45,lVar6,lVar1,
                             (undefined4)DAT_ffff0000be126e98);
          if ((uVar5 & 1) == 0) {
            FUN_ffff0000be05fd98
                      (s__s__d____s____d__erase_warmreset_ffff0000be0b3490,
                       s_WritePLLKWarmresetLog_ffff0000be0a9766,0x7b9,s_BLACKBOX_ffff0000be0aa9ee,
                       0x7b9);
          }
          else {
            if ((DAT_ffff0000be1261e0 & 0xfe) == 2) {
              uVar5 = FUN_ffff0000be04ed38();
              if ((uVar5 & 1) == 0) {
                FUN_ffff0000be05fd98
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000be0bc594,
                           s_WritePLLKWarmresetLog_ffff0000be0a9766,0x7c1,
                           s_BLACKBOX_ffff0000be0aa9ee,0x7c1,10);
                goto LAB_ffff0000be04f964;
              }
              uVar5 = FUN_ffff0000be04ee88();
              if ((uVar5 & 1) == 0) {
                FUN_ffff0000be05fd98
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000be0bc594,
                           s_WritePLLKWarmresetLog_ffff0000be0a9766,0x7c7,
                           s_BLACKBOX_ffff0000be0aa9ee,0x7c7,0xb);
                goto LAB_ffff0000be04f964;
              }
              uVar5 = FUN_ffff0000be04efd8();
              if ((uVar5 & 1) == 0) {
                FUN_ffff0000be05fd98
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000be0bc594,
                           s_WritePLLKWarmresetLog_ffff0000be0a9766,0x7cd,
                           s_BLACKBOX_ffff0000be0aa9ee,0x7cd,0xc);
                goto LAB_ffff0000be04f964;
              }
              uVar5 = FUN_ffff0000be04f198();
              if ((uVar5 & 1) == 0) {
                FUN_ffff0000be05fd98
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000be0bc594,
                           s_WritePLLKWarmresetLog_ffff0000be0a9766,0x7d4,
                           s_BLACKBOX_ffff0000be0aa9ee,0x7d4,0xd);
                goto LAB_ffff0000be04f964;
              }
              uVar5 = FUN_ffff0000be04f284();
              if ((uVar5 & 1) == 0) {
                FUN_ffff0000be05fd98
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000be0bc594,
                           s_WritePLLKWarmresetLog_ffff0000be0a9766,0x7db,
                           s_BLACKBOX_ffff0000be0aa9ee,0x7db,0xe);
                goto LAB_ffff0000be04f964;
              }
              uVar5 = FUN_ffff0000be04f370();
              if ((uVar5 & 1) == 0) {
                FUN_ffff0000be05fd98
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000be0bc594,
                           s_WritePLLKWarmresetLog_ffff0000be0a9766,0x7e1,
                           s_BLACKBOX_ffff0000be0aa9ee,0x7e1,0xf);
                goto LAB_ffff0000be04f964;
              }
              uVar5 = FUN_ffff0000be04f45c();
              if ((uVar5 & 1) == 0) {
                FUN_ffff0000be05fd98
                          (s__s__d____s____d__CheckBlackboxHe_ffff0000be0bc594,
                           s_WritePLLKWarmresetLog_ffff0000be0a9766,0x7e7,
                           s_BLACKBOX_ffff0000be0aa9ee,0x7e7,0x10);
                goto LAB_ffff0000be04f964;
              }
            }
            if ((DAT_ffff0000be1261e0 == 3) && (uVar5 = FUN_ffff0000be04f570(), (uVar5 & 1) == 0)) {
              FUN_ffff0000be05fd98
                        (s__s__d____s____d__CheckBlackboxHe_ffff0000be0bc594,
                         s_WritePLLKWarmresetLog_ffff0000be0a9766,0x7ef,s_BLACKBOX_ffff0000be0aa9ee,
                         0x7ef,0x11);
            }
            else {
              uVar5 = FUN_ffff0000be04d0b0
                                (s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126e20,0x400,0x3c);
              if ((uVar5 & 1) == 0) {
                FUN_ffff0000be05fd98
                          (s__s__d____s____d__write_aee_log_t_ffff0000be0bc5c5,
                           s_WritePLLKWarmresetLog_ffff0000be0a9766,0x7f7,
                           s_BLACKBOX_ffff0000be0aa9ee,0x7f7);
              }
              else {
                uVar5 = FUN_ffff0000be04d0b0
                                  (s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126e60,
                                   DAT_ffff0000be126e34,0x1c);
                if ((uVar5 & 1) == 0) {
                  FUN_ffff0000be05fd98
                            (s__s__d____s____d__write_aee_log_c_ffff0000be0a977c,
                             s_WritePLLKWarmresetLog_ffff0000be0a9766,0x7fc,
                             s_BLACKBOX_ffff0000be0aa9ee,0x7fc);
                }
                else {
                  uVar5 = FUN_ffff0000be04d0b0
                                    (s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126e80,uVar11,0x80);
                  if ((uVar5 & 1) == 0) {
                    FUN_ffff0000be05fd98
                              (s__s__d____s____d__write_aee_index_ffff0000be0bfea8,
                               s_WritePLLKWarmresetLog_ffff0000be0a9766,0x801,
                               s_BLACKBOX_ffff0000be0aa9ee,0x801);
                  }
                  else {
                    if (DAT_ffff0000be1261e0 != 1) {
                      FUN_ffff0000be04e7a4();
                    }
                    FUN_ffff0000be05fd98
                              (s__s__d____s___aee_GetPLLKlog_from_ffff0000be0a8705,
                               s_WritePLLKWarmresetLog_ffff0000be0a9766,0x808,
                               s_BLACKBOX_ffff0000be0aa9ee);
                    uVar7 = FUN_ffff0000be04e6ec(lVar1);
                    FUN_ffff0000be05fd98
                              (s__s__d____s___aee_GetPLLKlog_from_ffff0000be0b135f,
                               s_WritePLLKWarmresetLog_ffff0000be0a9766,0x80a,
                               s_BLACKBOX_ffff0000be0aa9ee,uVar7);
                  }
                }
              }
            }
          }
        }
        goto LAB_ffff0000be04f964;
      }
      uVar7 = 0x7a5;
      uVar8 = 0x7a5;
      uVar9 = 2;
    }
  }
  FUN_ffff0000be05fd98
            (s__s__d____s____d__CheckBlackboxHe_ffff0000be0bc594,
             s_WritePLLKWarmresetLog_ffff0000be0a9766,uVar7,s_BLACKBOX_ffff0000be0aa9ee,uVar8,uVar9)
  ;
  lVar6 = 0;
LAB_ffff0000be04f964:
  thunk_FUN_ffff0000be09937c(lVar6);
  return;
}


