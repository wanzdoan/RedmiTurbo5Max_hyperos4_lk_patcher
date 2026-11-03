// ffff000090737554  size=16  FUN_ffff000090737554

undefined8 FUN_ffff000090737554(long param_1)

{
  undefined8 uVar1;
  code *UNRECOVERED_JUMPTABLE;
  
  if (((param_1 != 0) && (*(undefined8 **)(param_1 + 8) != (undefined8 *)0x0)) &&
     (UNRECOVERED_JUMPTABLE = (code *)**(undefined8 **)(param_1 + 8),
     UNRECOVERED_JUMPTABLE != (code *)0x0)) {
    if (*(long *)(param_1 + 0x10) != 0) {
      param_1 = *(long *)(param_1 + 0x10);
    }
                    /* WARNING: Could not recover jumptable at 0xffff000090737580. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
    uVar1 = (*UNRECOVERED_JUMPTABLE)(param_1);
    return uVar1;
  }
  return 0xfffffff8;
}


