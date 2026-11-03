// ffff00009072e87c  size=508  FUN_ffff00009072e87c

long FUN_ffff00009072e87c(long param_1,uint param_2,uint *param_3)

{
  long lVar1;
  uint uVar2;
  int iVar3;
  char *pcVar4;
  undefined8 uVar5;
  long lVar6;
  uint uVar7;
  undefined8 local_50;
  uint5 uStack_48;
  undefined3 uStack_43;
  
  if ((param_2 & 3) != 0) {
    pcVar4 = s__CCCI___s__d__Check_header_size_a_ffff0000907e2b2e;
    uVar5 = 0xe0;
    uVar7 = param_2;
LAB_ffff00009072e8bc:
    FUN_ffff00009072ff90(pcVar4,s_get_check_header_ffff0000907f28eb,uVar5,uVar7);
    return 0;
  }
  lVar1 = param_1 + (ulong)param_2;
  FUN_ffff00009077de54
            (s__s__d___CCCI_tail_addr__p__ffff0000907d9113,s_get_check_header_ffff0000907f28eb,0xe5,
             lVar1);
  if (param_1 == 0) {
    FUN_ffff00009072ff90
              (s__CCCI___s__d__tail_addr_is_NULL_ffff0000907dde9a,
               s_get_check_header_ffff0000907f28eb,0xe7);
    return 0;
  }
  uVar7 = *(uint *)(lVar1 + -4);
  FUN_ffff00009077de54
            (s__s__d___CCCI_Check_headr_v1__u___ffff0000907e46e2,s_get_check_header_ffff0000907f28eb
             ,0xef,0xbc,0x11c,0x200,uVar7);
  if (uVar7 == 0xbc) {
    lVar6 = -0xbc;
    uVar2 = 1;
    if (param_2 < 0xbc) {
LAB_ffff00009072e9f8:
      pcVar4 = s__CCCI___s__d__MD_image_size__u__<_ffff0000907e2b6f;
      uVar5 = 0x100;
      goto LAB_ffff00009072ea14;
    }
  }
  else if (uVar7 == 0x200) {
    lVar6 = -0x200;
    uVar2 = 6;
    if (param_2 < 0x200) goto LAB_ffff00009072e9f8;
  }
  else {
    if (uVar7 != 0x11c) {
      pcVar4 = s__CCCI___s__d__Header_version_det_ffff0000907edeff;
      uVar5 = 0xfb;
      goto LAB_ffff00009072e8bc;
    }
    lVar6 = -0x11c;
    uVar2 = 3;
    if (param_2 < 0x11c) goto LAB_ffff00009072e9f8;
  }
  uVar7 = uVar2;
  lVar1 = lVar1 + lVar6;
  iVar3 = FUN_ffff00009077f080(lVar1,s_CHECK_HEADER_ffff0000907d0f7a,0xc);
  if (iVar3 != 0) {
    local_50 = 0;
    _uStack_48 = 0;
    FUN_ffff00009077e858(&local_50,lVar1,0xc);
    uStack_48 = (uint5)(uint)uStack_48;
    FUN_ffff00009072ff90
              (s__CCCI___s__d__Invalid_md_check_h_ffff0000907fee32,
               s_get_check_header_ffff0000907f28eb,0x10d,&local_50);
    return 0;
  }
  param_2 = *(uint *)(lVar1 + 0xc);
  if (param_2 == uVar7) {
    FUN_ffff00009072eb98(lVar1,uVar7);
    if (param_3 != (uint *)0x0) {
      *param_3 = uVar7;
      return lVar1;
    }
    return lVar1;
  }
  pcVar4 = s__CCCI___s__d__Version_abnormal___ffff0000907cf601;
  uVar5 = 0x113;
LAB_ffff00009072ea14:
  FUN_ffff00009072ff90(pcVar4,s_get_check_header_ffff0000907f28eb,uVar5,param_2,uVar7);
  return 0;
}


