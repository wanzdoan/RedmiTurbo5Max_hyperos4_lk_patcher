// ffff000090728048  size=64  FUN_ffff000090728048

void FUN_ffff000090728048(code *param_1)

{
  ulong uVar1;
  
  uVar1 = (*param_1)(0x13e000,0x800);
  if ((uVar1 & 1) != 0) {
    return;
  }
  FUN_ffff00009077de54(s__Rotfw__log_dump_fail_ffff0000907d773f);
  return;
}


