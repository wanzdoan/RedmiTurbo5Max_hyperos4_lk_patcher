// ffff0000907c183c  size=788  FUN_ffff0000907c183c

long FUN_ffff0000907c183c(char *param_1,char *param_2,undefined8 param_3)

{
  bool bVar1;
  char cVar2;
  char *pcVar3;
  char *pcVar4;
  long lVar5;
  long lVar6;
  long lVar7;
  char *pcVar8;
  long lVar9;
  long lVar10;
  long lVar11;
  long lVar12;
  
  lVar5 = thunk_FUN_ffff00009077f250(param_2);
  lVar6 = thunk_FUN_ffff00009077f250(param_3);
  cVar2 = *param_1;
  if (cVar2 != '\0') {
    lVar9 = 0;
    lVar11 = 0;
LAB_ffff0000907c18e4:
    lVar12 = 0;
    pcVar8 = param_1;
    do {
      pcVar8 = pcVar8 + 1;
      if (cVar2 == *param_2) {
        cVar2 = param_2[1];
        pcVar3 = param_2 + 2;
        pcVar4 = pcVar8;
        while( true ) {
          if (cVar2 == '\0') {
            lVar10 = lVar12 + lVar6;
            if (lVar9 == 0) {
              lVar7 = FUN_ffff0000907c0f94(lVar10 + 1);
              if (lVar7 == 0) {
                pcVar8 = s_external_lib_libavb_avb_util_c_ffff0000907d5585;
                lVar6 = thunk_FUN_ffff00009077f250
                                  (s_external_lib_libavb_avb_util_c_ffff0000907d5585);
                lVar5 = lVar6 + -2;
                if (lVar6 + -2 < 0) goto LAB_ffff0000907c1b1c;
                goto LAB_ffff0000907c1af4;
              }
              thunk_FUN_ffff00009077eb20(lVar7,param_1,lVar12);
              thunk_FUN_ffff00009077eb20(lVar7 + lVar12,param_3,lVar6);
              *(undefined1 *)(lVar7 + lVar10) = 0;
            }
            else {
              lVar10 = lVar10 + lVar11;
              lVar7 = FUN_ffff0000907c0f94(lVar10 + 1);
              if (lVar7 == 0) {
                pcVar8 = s_external_lib_libavb_avb_util_c_ffff0000907d5585;
                lVar6 = thunk_FUN_ffff00009077f250
                                  (s_external_lib_libavb_avb_util_c_ffff0000907d5585);
                lVar5 = lVar6 + -2;
                if (lVar6 + -2 < 0) goto LAB_ffff0000907c1a88;
                goto LAB_ffff0000907c1a60;
              }
              thunk_FUN_ffff00009077eb20(lVar7,lVar9,lVar11);
              thunk_FUN_ffff00009077eb20(lVar7 + lVar11,param_1,lVar12);
              thunk_FUN_ffff00009077eb20(lVar7 + lVar11 + lVar12,param_3,lVar6);
              *(undefined1 *)(lVar7 + lVar10) = 0;
              FUN_ffff0000907c1014(lVar9);
            }
            param_1 = param_1 + lVar5 + lVar12;
            cVar2 = *param_1;
            lVar9 = lVar7;
            lVar11 = lVar10;
            if (cVar2 != '\0') goto LAB_ffff0000907c18e4;
            goto LAB_ffff0000907c1998;
          }
          if (*pcVar4 != cVar2) break;
          cVar2 = *pcVar3;
          pcVar3 = pcVar3 + 1;
          pcVar4 = pcVar4 + 1;
        }
      }
      lVar12 = lVar12 + 1;
      cVar2 = param_1[lVar12];
    } while (cVar2 != '\0');
    if (lVar9 != 0) {
LAB_ffff0000907c1998:
      lVar5 = thunk_FUN_ffff00009077f250(param_1);
      lVar6 = FUN_ffff0000907c0f94(lVar5 + lVar11 + 1);
      if (lVar6 != 0) {
        thunk_FUN_ffff00009077eb20(lVar6,lVar9,lVar11);
        thunk_FUN_ffff00009077eb20(lVar6 + lVar11,param_1,lVar5);
        *(undefined1 *)(lVar6 + lVar5 + lVar11) = 0;
        FUN_ffff0000907c1014(lVar9);
        return lVar6;
      }
      pcVar8 = s_external_lib_libavb_avb_util_c_ffff0000907d5585;
      lVar6 = thunk_FUN_ffff00009077f250(s_external_lib_libavb_avb_util_c_ffff0000907d5585);
      lVar5 = lVar6 + -2;
      if (-1 < lVar6 + -2) {
        do {
          if (s_external_lib_libavb_avb_util_c_ffff0000907d5585[lVar5] == '/')
          goto LAB_ffff0000907c1a80;
          bVar1 = 0 < lVar5;
          lVar5 = lVar5 + -1;
        } while (bVar1);
      }
      goto LAB_ffff0000907c1a88;
    }
  }
  lVar5 = FUN_ffff0000907c1690(param_1);
  return lVar5;
LAB_ffff0000907c1a80:
  pcVar8 = s_external_lib_libavb_avb_util_c_ffff0000907d5585 + lVar5 + 1;
  goto LAB_ffff0000907c1a88;
  while (bVar1 = 0 < lVar5, lVar5 = lVar5 + -1, bVar1) {
LAB_ffff0000907c1af4:
    if (s_external_lib_libavb_avb_util_c_ffff0000907d5585[lVar5] == '/') {
      pcVar8 = s_external_lib_libavb_avb_util_c_ffff0000907d5585 + lVar5 + 1;
      break;
    }
  }
LAB_ffff0000907c1b1c:
  FUN_ffff0000907c0ee8
            (pcVar8,&DAT_ffff0000907ebfa6,&DAT_ffff0000907e23db,s___ERROR__ffff0000907e07dc,
             s_Failed_to_allocate_memory__ffff00009080366c,0);
  return 0;
  while (bVar1 = 0 < lVar5, lVar5 = lVar5 + -1, bVar1) {
LAB_ffff0000907c1a60:
    if (s_external_lib_libavb_avb_util_c_ffff0000907d5585[lVar5] == '/') goto LAB_ffff0000907c1a80;
  }
LAB_ffff0000907c1a88:
  FUN_ffff0000907c0ee8
            (pcVar8,&DAT_ffff0000907ebfa6,&DAT_ffff0000907e23db,s___ERROR__ffff0000907e07dc,
             s_Failed_to_allocate_memory__ffff00009080366c,0);
  return lVar9;
}


