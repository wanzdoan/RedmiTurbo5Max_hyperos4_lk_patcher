// File offset entry: 0x7e444

ulong FUN_0207e344(long param_1)

{
  long lVar1;
  ulong uVar2;
  
  if (param_1 == 0) {
    func_0x02063328(0x200002);
    uVar2 = 0xffffffff;
  }
  else {
    lVar1 = func_0x0207e380();
    uVar2 = (ulong)(lVar1 != 0);
  }
  return uVar2;
}


