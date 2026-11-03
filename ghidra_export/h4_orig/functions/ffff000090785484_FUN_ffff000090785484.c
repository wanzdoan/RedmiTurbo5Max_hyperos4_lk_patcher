// ffff000090785484  size=268  FUN_ffff000090785484

int FUN_ffff000090785484
              (undefined8 param_1,undefined8 param_2,code *param_3,long *param_4,undefined4 *param_5
              )

{
  code *pcVar1;
  int iVar2;
  undefined4 uVar3;
  long lVar4;
  long lVar5;
  undefined4 local_48;
  uint local_44;
  
  lVar4 = FUN_ffff00009077d514(0x100000);
  if (lVar4 == 0) {
    return -5;
  }
  iVar2 = FUN_ffff000090785d18(param_1,param_2,lVar4,0x100000);
  if (-1 < iVar2) {
    local_44 = 0;
    pcVar1 = FUN_ffff00009078533c;
    if (param_3 != (code *)0x0) {
      pcVar1 = param_3;
    }
    local_48 = 0;
    uVar3 = FUN_ffff000090703444();
    iVar2 = (*pcVar1)(lVar4,uVar3,&DAT_ffff000090832988,&local_48,&local_44);
    if (-1 < iVar2) {
      lVar5 = FUN_ffff00009077d514(local_48);
      if (lVar5 != 0) {
        *param_5 = local_48;
        FUN_ffff00009077eb20(lVar5,lVar4 + (ulong)local_44);
        *param_4 = lVar5;
        goto LAB_ffff000090785568;
      }
      iVar2 = -5;
    }
    FUN_ffff00009077e11c(s_dtbo_parse_fail_ffff0000907f1f1c);
  }
LAB_ffff000090785568:
  thunk_FUN_ffff0000907c314c(lVar4);
  return iVar2;
}


