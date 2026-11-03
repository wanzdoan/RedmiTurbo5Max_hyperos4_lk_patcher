
void FUN_ffff0000b8007e8c(undefined8 param_1)

{
  undefined8 uVar1;
  undefined8 uVar2;
  
  FUN_ffff0000b804c33c(s_invalid_exception__which_0x_x_ffff0000b809d30a);
  uVar1 = esr_el1;
  uVar2 = far_el1;
  FUN_ffff0000b804c33c(s_ESR__0x_llx_FAR__0x_llx_ffff0000b809514b,uVar1,uVar2);
  FUN_ffff0000b8007cb4(param_1);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b744(&DAT_ffff0000b80930f8);
}


