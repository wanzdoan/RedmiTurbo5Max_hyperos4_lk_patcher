
undefined8 FUN_ffff00009072ed10(undefined8 param_1,undefined8 param_2,int param_3)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  undefined8 uVar4;
  int local_24;
  
  local_24 = 0;
  lVar2 = FUN_ffff00009072eb14(param_1,param_2,&local_24);
  if (lVar2 != 0) {
    if (local_24 == 3) {
      iVar1 = FUN_ffff000090730738(param_3,*(undefined4 *)(lVar2 + 0xb0),0x100,0);
      if (iVar1 < 0) {
        pcVar3 = s__CCCI___s__d__Update_DSP_RO_fail_ffff0000907e2c90;
        uVar4 = 0x1a9;
      }
      else {
        iVar1 = FUN_ffff000090730738
                          (*(int *)(lVar2 + 0xb0) + param_3,
                           *(int *)(lVar2 + 0xac) - *(int *)(lVar2 + 0xb0),0x200,0);
        if (-1 < iVar1) {
          return 0;
        }
        pcVar3 = s__CCCI___s__d__Update_DSP_RW_fail_ffff0000907f2b24;
        uVar4 = 0x1b0;
      }
      FUN_ffff000090730228(pcVar3,s_ass_func_for_dsp_normal_img_ffff000090800aac,uVar4);
    }
    else {
      FUN_ffff000090730228
                (s__CCCI___s__d__not_support_header_ffff0000907fefad,
                 s_ccci_dsp_img_parsing_ffff0000907d0fa2,0x1df);
    }
  }
  return 0xffffffff;
}


