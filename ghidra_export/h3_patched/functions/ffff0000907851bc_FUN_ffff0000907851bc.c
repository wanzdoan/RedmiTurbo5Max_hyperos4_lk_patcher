// ffff0000907851bc  size=268  FUN_ffff0000907851bc

int FUN_ffff0000907851bc
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
  
  lVar4 = FUN_ffff00009077d24c(0x100000);
  if (lVar4 == 0) {
    return -5;
  }
  iVar2 = FUN_ffff000090785a50(param_1,param_2,lVar4,0x100000);
  if (-1 < iVar2) {
    local_44 = 0;
    pcVar1 = FUN_ffff000090785074;
    if (param_3 != (code *)0x0) {
      pcVar1 = param_3;
    }
    local_48 = 0;
    uVar3 = FUN_ffff000090703444();
    iVar2 = (*pcVar1)(lVar4,uVar3,&DAT_ffff000090832968,&local_48,&local_44);
    if (-1 < iVar2) {
      lVar5 = FUN_ffff00009077d24c(local_48);
      if (lVar5 != 0) {
        *param_5 = local_48;
        FUN_ffff00009077e858(lVar5,lVar4 + (ulong)local_44);
        *param_4 = lVar5;
        goto LAB_ffff0000907852a0;
      }
      iVar2 = -5;
    }
    FUN_ffff00009077de54(s_dtbo_parse_fail_ffff0000907f1d79);
  }
LAB_ffff0000907852a0:
  thunk_FUN_ffff0000907c2ad8(lVar4);
  return iVar2;
}


