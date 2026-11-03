
undefined8 * FUN_ffff000090770ba8(undefined8 param_1,int *param_2,undefined8 param_3)

{
  uint uVar1;
  undefined8 *puVar2;
  long lVar3;
  
  puVar2 = (undefined8 *)FUN_ffff000090771578(param_3);
  if (*param_2 == 0x3000000) {
    if (puVar2 != (undefined8 *)0x0) {
      uVar1 = param_2[2];
      *puVar2 = param_2;
      puVar2[1] = 0;
      uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
      lVar3 = FUN_ffff00009076d3d4(param_1,uVar1 >> 0x10 | uVar1 << 0x10);
      puVar2[2] = lVar3;
      if (lVar3 == 0) {
        FUN_ffff00009076fec8(s_ERROR___s____ffff0000907f36ba,s_ufdt_node_construct_ffff0000907d1d35)
        ;
        FUN_ffff00009076fec8(s_Failed_to_get_property_name_ffff0000907e5436);
        FUN_ffff00009077165c(param_3,puVar2);
        puVar2 = (undefined8 *)0x0;
      }
    }
  }
  else if (puVar2 != (undefined8 *)0x0) {
    *puVar2 = param_2;
    puVar2[1] = 0;
    puVar2[2] = 0;
    puVar2[3] = puVar2 + 2;
  }
  return puVar2;
}


