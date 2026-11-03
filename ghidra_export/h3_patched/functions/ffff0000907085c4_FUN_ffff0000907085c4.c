// ffff0000907085c4  size=72  FUN_ffff0000907085c4

void FUN_ffff0000907085c4(undefined8 param_1)

{
  undefined8 uVar1;
  undefined8 uVar2;
  
  FUN_ffff00009077de54(s_invalid_exception__which_0x_x_ffff0000907fae49);
  uVar1 = esr_el1;
  uVar2 = far_el1;
  FUN_ffff00009077de54(s_ESR__0x_llx_FAR__0x_llx_ffff0000907ebcf1,uVar1,uVar2);
  FUN_ffff0000907083ec(param_1);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cc18(&DAT_ffff0000907e8b04);
}


