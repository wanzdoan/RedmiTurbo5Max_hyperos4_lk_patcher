// File offset entry: 0xaa128

undefined8 FUN_020aa028(long param_1,undefined8 param_2)

{
  if (param_1 != 0) {
    FUN_02091664(param_1,0,0x80);
    *(undefined8 *)(param_1 + 0x40) = param_2;
    return 0;
  }
  return 0x7247;
}


