
ulong FUN_ffff000090736438(void)

{
  long lVar1;
  ushort local_14 [2];
  
  if (DAT_ffff00009086dbe8 != (undefined8 *)0x0) {
    lVar1 = DAT_ffff00009086dbe8[2];
    local_14[0] = 0;
    (*(code *)*DAT_ffff00009086dbe8)
              (*(undefined2 *)(lVar1 + 0x1e),local_14,*(undefined2 *)(lVar1 + 0x20),
               *(undefined2 *)(lVar1 + 0x22));
    return (ulong)local_14[0];
  }
  return 0xffffffda;
}


