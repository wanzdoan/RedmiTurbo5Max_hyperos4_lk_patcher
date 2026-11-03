// ffff0000907c2f50  size=36  FUN_ffff0000907c2f50

undefined8 FUN_ffff0000907c2f50(long param_1)

{
  undefined8 uVar1;
  
  if ((code *)**(undefined8 **)(param_1 + 8) != (code *)0x0) {
                    /* WARNING: Could not recover jumptable at 0xffff0000907c2f64. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
    uVar1 = (*(code *)**(undefined8 **)(param_1 + 8))();
    return uVar1;
  }
  return 0xffffffffffffffe8;
}


