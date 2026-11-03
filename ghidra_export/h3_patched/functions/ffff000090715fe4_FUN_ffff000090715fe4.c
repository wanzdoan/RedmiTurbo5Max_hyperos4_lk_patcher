// ffff000090715fe4  size=152  FUN_ffff000090715fe4

void FUN_ffff000090715fe4(uint param_1,undefined8 param_2,undefined8 param_3)

{
  undefined8 *puVar1;
  undefined8 uVar2;
  ulong uVar3;
  ulong uVar4;
  
  uVar4 = (ulong)param_1;
  if (param_1 < 0x3e0) {
    uVar2 = daif;
    if (((uint)uVar2 >> 7 & 1) == 0) {
      uVar3 = daif;
      daif = uVar3 | 0x80;
      puVar1 = (undefined8 *)(uVar4 * 0x10 + -0xffff6f7c07c8);
      if (0x1f < param_1) {
        puVar1 = (undefined8 *)((ulong)(param_1 - 0x20) * 0x10 + -0xffff6f7c05c8);
      }
      *puVar1 = param_2;
      puVar1[1] = param_3;
      uVar4 = daif;
      daif = uVar4 & 0xffffffffffffff7f;
    }
    else {
      puVar1 = (undefined8 *)(uVar4 * 0x10 + -0xffff6f7c07c8);
      if (0x1f < param_1) {
        puVar1 = (undefined8 *)((ulong)(param_1 - 0x20) * 0x10 + -0xffff6f7c05c8);
      }
      *puVar1 = param_2;
      puVar1[1] = param_3;
    }
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cc18
            (s__s__vector_out_of_range__d_ffff0000907dc19b,s_register_int_handler_ffff0000907f9425,
             uVar4);
}


