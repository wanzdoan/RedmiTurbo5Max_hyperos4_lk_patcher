// ffff000090770a10  size=144  FUN_ffff000090770a10

undefined4 FUN_ffff000090770a10(undefined8 *param_1,undefined8 *param_2)

{
  uint uVar1;
  
  if (((param_1 != (undefined8 *)0x0) && (param_2 != (undefined8 *)0x0)) &&
     (*(int *)*param_1 == 0x1000000)) {
    uVar1 = (*(uint *)*param_2 & 0xff00ff00) >> 8 | (*(uint *)*param_2 & 0xff00ff) << 8;
    if ((uVar1 >> 0x10 | uVar1 << 0x10 | 2) == 3) {
      *(undefined8 **)param_1[3] = param_2;
      param_1[3] = param_2 + 1;
      return 0;
    }
    FUN_ffff00009076fc00(s_ERROR___s____ffff0000907f352e,s_ufdt_node_add_child_ffff0000907fc2c5);
    FUN_ffff00009076fc00(s_invalid_children_tag_type_ffff0000907f3551);
  }
  return 0xffffffff;
}


