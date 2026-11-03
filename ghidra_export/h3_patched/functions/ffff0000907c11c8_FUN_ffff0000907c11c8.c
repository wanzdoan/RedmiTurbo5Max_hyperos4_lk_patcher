// ffff0000907c11c8  size=788  FUN_ffff0000907c11c8

long FUN_ffff0000907c11c8(char *param_1,char *param_2,undefined8 param_3)

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
  
  lVar5 = thunk_FUN_ffff00009077ef88(param_2);
  lVar6 = thunk_FUN_ffff00009077ef88(param_3);
  cVar2 = *param_1;
  if (cVar2 != '\0') {
    lVar9 = 0;
    lVar11 = 0;
LAB_ffff0000907c1270:
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
              lVar7 = FUN_ffff0000907c0920(lVar10 + 1);
              if (lVar7 == 0) {
                pcVar8 = s_external_lib_libavb_avb_util_c_ffff0000907d5548;
                lVar6 = thunk_FUN_ffff00009077ef88
                                  (s_external_lib_libavb_avb_util_c_ffff0000907d5548);
                lVar5 = lVar6 + -2;
                if (lVar6 + -2 < 0) goto LAB_ffff0000907c14a8;
                goto LAB_ffff0000907c1480;
              }
              thunk_FUN_ffff00009077e858(lVar7,param_1,lVar12);
              thunk_FUN_ffff00009077e858(lVar7 + lVar12,param_3,lVar6);
              *(undefined1 *)(lVar7 + lVar10) = 0;
            }
            else {
              lVar10 = lVar10 + lVar11;
              lVar7 = FUN_ffff0000907c0920(lVar10 + 1);
              if (lVar7 == 0) {
                pcVar8 = s_external_lib_libavb_avb_util_c_ffff0000907d5548;
                lVar6 = thunk_FUN_ffff00009077ef88
                                  (s_external_lib_libavb_avb_util_c_ffff0000907d5548);
                lVar5 = lVar6 + -2;
                if (lVar6 + -2 < 0) goto LAB_ffff0000907c1414;
                goto LAB_ffff0000907c13ec;
              }
              thunk_FUN_ffff00009077e858(lVar7,lVar9,lVar11);
              thunk_FUN_ffff00009077e858(lVar7 + lVar11,param_1,lVar12);
              thunk_FUN_ffff00009077e858(lVar7 + lVar11 + lVar12,param_3,lVar6);
              *(undefined1 *)(lVar7 + lVar10) = 0;
              FUN_ffff0000907c09a0(lVar9);
            }
            param_1 = param_1 + lVar5 + lVar12;
            cVar2 = *param_1;
            lVar9 = lVar7;
            lVar11 = lVar10;
            if (cVar2 != '\0') goto LAB_ffff0000907c1270;
            goto LAB_ffff0000907c1324;
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
LAB_ffff0000907c1324:
      lVar5 = thunk_FUN_ffff00009077ef88(param_1);
      lVar6 = FUN_ffff0000907c0920(lVar5 + lVar11 + 1);
      if (lVar6 != 0) {
        thunk_FUN_ffff00009077e858(lVar6,lVar9,lVar11);
        thunk_FUN_ffff00009077e858(lVar6 + lVar11,param_1,lVar5);
        *(undefined1 *)(lVar6 + lVar5 + lVar11) = 0;
        FUN_ffff0000907c09a0(lVar9);
        return lVar6;
      }
      pcVar8 = s_external_lib_libavb_avb_util_c_ffff0000907d5548;
      lVar6 = thunk_FUN_ffff00009077ef88(s_external_lib_libavb_avb_util_c_ffff0000907d5548);
      lVar5 = lVar6 + -2;
      if (-1 < lVar6 + -2) {
        do {
          if (s_external_lib_libavb_avb_util_c_ffff0000907d5548[lVar5] == '/')
          goto LAB_ffff0000907c140c;
          bVar1 = 0 < lVar5;
          lVar5 = lVar5 + -1;
        } while (bVar1);
      }
      goto LAB_ffff0000907c1414;
    }
  }
  lVar5 = FUN_ffff0000907c101c(param_1);
  return lVar5;
LAB_ffff0000907c140c:
  pcVar8 = s_external_lib_libavb_avb_util_c_ffff0000907d5548 + lVar5 + 1;
  goto LAB_ffff0000907c1414;
  while (bVar1 = 0 < lVar5, lVar5 = lVar5 + -1, bVar1) {
LAB_ffff0000907c1480:
    if (s_external_lib_libavb_avb_util_c_ffff0000907d5548[lVar5] == '/') {
      pcVar8 = s_external_lib_libavb_avb_util_c_ffff0000907d5548 + lVar5 + 1;
      break;
    }
  }
LAB_ffff0000907c14a8:
  FUN_ffff0000907c0874
            (pcVar8,&DAT_ffff0000907ebe0c,&DAT_ffff0000907e22f3,s___ERROR__ffff0000907e0722,
             s_Failed_to_allocate_memory__ffff000090803444,0);
  return 0;
  while (bVar1 = 0 < lVar5, lVar5 = lVar5 + -1, bVar1) {
LAB_ffff0000907c13ec:
    if (s_external_lib_libavb_avb_util_c_ffff0000907d5548[lVar5] == '/') goto LAB_ffff0000907c140c;
  }
LAB_ffff0000907c1414:
  FUN_ffff0000907c0874
            (pcVar8,&DAT_ffff0000907ebe0c,&DAT_ffff0000907e22f3,s___ERROR__ffff0000907e0722,
             s_Failed_to_allocate_memory__ffff000090803444,0);
  return lVar9;
}


