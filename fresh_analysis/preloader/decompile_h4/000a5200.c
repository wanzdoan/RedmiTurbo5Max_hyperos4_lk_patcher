// File offset entry: 0xa5200

int FUN_020a5100(void)

{
  int iVar1;
  long unaff_x22;
  int iVar2;
  int unaff_w26;
  long unaff_x29;
  
  iVar2 = unaff_w26 + -0xffd;
  do {
    func_0x0208ada0(0x20cdbba,0x20c9773);
    func_0x0208ada0(0x20d01cb,iVar2);
    while( true ) {
      if (*plRam0000000002103280 == *(long *)(unaff_x29 + -8)) {
        return iVar2;
      }
      func_0x02091090();
      iVar1 = func_0x020a0e58(unaff_x22 + 0x3c,unaff_x29 + -0x28,0x20);
      iVar2 = 0x101003;
      if (iVar1 != 0) break;
      func_0x0208ada0(0x20cdbba,0x20c9773);
      func_0x0208ada0(0x20cc927);
      iVar2 = 0;
    }
  } while( true );
}


