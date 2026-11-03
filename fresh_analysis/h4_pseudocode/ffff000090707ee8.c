
void FUN_ffff000090707ee8(long *param_1,code *param_2)

{
  long *plVar1;
  long lVar2;
  long *plVar3;
  
  plVar3 = (long *)&stack0xffffffffffffffd0;
  if (param_1 == (long *)0x0) {
    FUN_ffff00009077e11c(s_stack_trace__ffff0000907d72ee);
    if (&stack0x00000000 == (undefined1 *)0x30) {
      return;
    }
    param_2 = FUN_ffff000090707ee8;
  }
  else {
    FUN_ffff00009077e11c(s_stack_trace__ffff0000907d72ee);
    plVar3 = param_1;
  }
  while( true ) {
    FUN_ffff00009077e11c(s_0x_llx_ffff000090801dc3,(ulong)param_2 | 0xffff000000000000);
    lVar2 = FUN_ffff00009077b334(plVar3);
    if (lVar2 == 0) {
      FUN_ffff00009077e11c(s_frame_fp_is_invalid_address_ffff0000907fe986);
      return;
    }
    if (((ulong)plVar3 & 0xf) != 0) break;
    plVar1 = (long *)*plVar3;
    param_2 = (code *)plVar3[1];
    plVar3 = plVar1;
    if (plVar1 == (long *)0x0) {
      return;
    }
  }
  return;
}


