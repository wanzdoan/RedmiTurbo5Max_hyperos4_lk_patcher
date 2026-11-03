
undefined4 FUN_ffff0000b80785e0(undefined8 *param_1,undefined8 *param_2)

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
    FUN_ffff0000b80777d0(s_ERROR___s____ffff0000b809976e,s_ufdt_node_add_child_ffff0000b809e10c);
    FUN_ffff0000b80777d0(s_invalid_children_tag_type_ffff0000b8099791);
  }
  return 0xffffffff;
}


