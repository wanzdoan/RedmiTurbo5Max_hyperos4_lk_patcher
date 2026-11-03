
undefined8 FUN_ffff000090746fd0(undefined8 param_1,long param_2,long param_3)

{
  undefined8 uVar1;
  
  if (param_2 == 0) {
    uVar1 = 0xffffffff;
  }
  else {
    uVar1 = FUN_ffff00009076da24(param_1);
  }
  if (((param_3 != 0) && ((int)uVar1 < 0)) &&
     (uVar1 = FUN_ffff00009076da24(param_1,param_3), (int)uVar1 < 0)) {
    FUN_ffff00009077e11c
              (s___s___s_offset_is_not_found_ffff0000907d6439,
               s_connsys_get_fdt_node_ffff0000907de446,param_3);
    uVar1 = 0xffffffff;
  }
  return uVar1;
}


