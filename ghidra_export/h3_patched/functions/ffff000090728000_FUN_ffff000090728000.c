// ffff000090728000  size=4  FUN_ffff000090728000

void FUN_ffff000090728000(code *param_1)

{
  ulong uVar1;
  
  uVar1 = (*param_1)(0x112000,0x400);
  if ((uVar1 & 1) != 0) {
    return;
  }
  FUN_ffff00009077de54(s__Rot__log_dump_fail_ffff0000907f4472);
  return;
}


