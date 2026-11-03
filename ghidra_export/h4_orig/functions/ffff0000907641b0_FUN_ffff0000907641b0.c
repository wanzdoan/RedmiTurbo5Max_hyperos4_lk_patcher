// ffff0000907641b0  size=736  FUN_ffff0000907641b0

undefined8 FUN_ffff0000907641b0(ulong param_1,undefined1 *param_2,undefined1 *param_3,uint param_4)

{
  ulong uVar1;
  undefined1 uVar2;
  int iVar3;
  int iVar4;
  ulong uVar5;
  ulong uVar6;
  ulong uVar7;
  char *pcVar8;
  undefined8 uVar9;
  ulong uVar10;
  ulong uVar11;
  ulong uVar12;
  ulong uVar13;
  undefined1 auStack_268 [520];
  
  if ((param_1 == 0) || (param_4 == 0)) {
    pcVar8 = s__s__d__Inputed_parameter_is_not_i_ffff00009080151b;
    uVar9 = 0x6fc;
  }
  else {
    if (param_2 != (undefined1 *)0x0) {
      uVar1 = param_1 + param_4;
LAB_ffff0000907641f4:
      uVar5 = FUN_ffff00009077f250(param_2);
      if (uVar5 == 0) goto LAB_ffff000090764298;
      uVar6 = FUN_ffff00009077f250(param_1);
      if (uVar5 <= uVar6) {
        uVar13 = 0;
        uVar7 = param_1;
        do {
          uVar2 = *param_2;
          iVar3 = FUN_ffff00009077db9c(*(undefined1 *)(param_1 + uVar13));
          iVar4 = FUN_ffff00009077db9c(uVar2);
          if (iVar3 == iVar4) {
            uVar12 = 1;
            do {
              uVar10 = uVar12;
              if (uVar5 == uVar10) break;
              uVar2 = param_2[uVar10];
              iVar3 = FUN_ffff00009077db9c(*(undefined1 *)(uVar7 + uVar10));
              iVar4 = FUN_ffff00009077db9c(uVar2);
              uVar12 = uVar10 + 1;
            } while (iVar3 == iVar4);
            if (uVar5 <= uVar10) goto LAB_ffff000090764290;
          }
          uVar13 = uVar13 + 1;
          uVar7 = uVar7 + 1;
          if (uVar6 - uVar5 < uVar13) break;
        } while( true );
      }
    }
    pcVar8 = s__s__d__do_not_find_the_matched_p_ffff0000907f3585;
    uVar9 = 0x706;
  }
  FUN_ffff00009077e11c(pcVar8,s_Search_KeyWord_In_Buffer_ffff0000907dd10c,uVar9);
  return 0;
LAB_ffff000090764290:
  param_1 = param_1 + uVar13;
LAB_ffff000090764298:
  uVar5 = FUN_ffff00009077f074(param_1,10);
  if (uVar5 == 0) {
    FUN_ffff00009077e11c
              (s__s__d__reach_to_the_end_of_buffe_ffff0000907d0008,
               s_Search_KeyWord_In_Buffer_ffff0000907dd10c,0x70b);
    uVar5 = uVar1;
  }
  if (param_3 == (undefined1 *)0x0) {
    pcVar8 = s__s__d__find_the_matched_prefix____ffff0000907ff9f7;
    uVar9 = 0x711;
LAB_ffff000090764484:
    FUN_ffff00009077e11c(pcVar8,s_Search_KeyWord_In_Buffer_ffff0000907dd10c,uVar9,param_2);
    return 1;
  }
  if (param_1 < uVar5) {
    uVar13 = FUN_ffff00009077f250(param_3);
    uVar6 = param_1;
    if (uVar13 == 0) {
LAB_ffff000090764378:
      if (uVar6 < uVar5) {
        FUN_ffff00009077ee50(auStack_268,0,0x200);
        uVar5 = uVar5 - param_1;
        if (0x1fe < uVar5) {
          uVar5 = 0x1ff;
        }
        FUN_ffff00009077f2ac(auStack_268,param_1,uVar5);
        auStack_268[uVar5] = 0;
        pcVar8 = s__s__d__find_the_matched_line___s_ffff0000907e8374;
        param_2 = auStack_268;
        uVar9 = 0x720;
        goto LAB_ffff000090764484;
      }
    }
    else {
      uVar7 = FUN_ffff00009077f250(param_1);
      if (uVar13 <= uVar7) {
        uVar6 = 0;
        uVar12 = param_1;
        do {
          uVar2 = *param_3;
          iVar3 = FUN_ffff00009077db9c(*(undefined1 *)(param_1 + uVar6));
          iVar4 = FUN_ffff00009077db9c(uVar2);
          if (iVar3 == iVar4) {
            uVar10 = 1;
            do {
              uVar11 = uVar10;
              if (uVar13 == uVar11) break;
              uVar2 = param_3[uVar11];
              iVar3 = FUN_ffff00009077db9c(*(undefined1 *)(uVar12 + uVar11));
              iVar4 = FUN_ffff00009077db9c(uVar2);
              uVar10 = uVar11 + 1;
            } while (iVar3 == iVar4);
            if (uVar13 <= uVar11) {
              uVar6 = param_1 + uVar6;
              goto LAB_ffff000090764378;
            }
          }
          uVar6 = uVar6 + 1;
          uVar12 = uVar12 + 1;
        } while (uVar6 <= uVar7 - uVar13);
      }
    }
  }
  if (uVar5 == uVar1) {
    return 0;
  }
  FUN_ffff00009077e11c
            (s__s__d__begin_to_search_next_line_ffff0000907de98c,
             s_Search_KeyWord_In_Buffer_ffff0000907dd10c,0x729);
  param_1 = uVar5 + 1;
  if (uVar1 <= param_1) {
    return 0;
  }
  goto LAB_ffff0000907641f4;
}


