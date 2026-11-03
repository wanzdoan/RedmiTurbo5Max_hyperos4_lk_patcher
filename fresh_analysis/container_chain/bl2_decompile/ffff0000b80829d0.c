
undefined8 FUN_ffff0000b80829d0(long param_1)

{
  undefined8 uVar1;
  
  if ((code *)**(undefined8 **)(param_1 + 8) != (code *)0x0) {
                    /* WARNING: Could not recover jumptable at 0xffff0000b80829e4. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
    uVar1 = (*(code *)**(undefined8 **)(param_1 + 8))();
    return uVar1;
  }
  return 0xffffffffffffffe8;
}


