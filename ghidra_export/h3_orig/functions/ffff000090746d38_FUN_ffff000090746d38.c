// ffff000090746d38  size=116  FUN_ffff000090746d38

undefined8 FUN_ffff000090746d38(undefined8 param_1,long param_2,long param_3)

{
  undefined8 uVar1;
  
  if (param_2 == 0) {
    uVar1 = 0xffffffff;
  }
  else {
    uVar1 = FUN_ffff00009076d75c(param_1);
  }
  if (((param_3 != 0) && ((int)uVar1 < 0)) &&
     (uVar1 = FUN_ffff00009076d75c(param_1,param_3), (int)uVar1 < 0)) {
    FUN_ffff00009077de54
              (s___s___s_offset_is_not_found_ffff0000907d63a3,
               s_connsys_get_fdt_node_ffff0000907de3a3,param_3);
    uVar1 = 0xffffffff;
  }
  return uVar1;
}


