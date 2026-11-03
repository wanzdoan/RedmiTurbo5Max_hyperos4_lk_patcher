
char * ld_md_errno_to_str(uint param_1)

{
  uint uVar1;
  code *pcVar2;
  
  if (param_1 == 0) {
    return "Success";
  }
  uVar1 = -param_1;
  if (-1 < (int)param_1) {
    uVar1 = param_1;
  }
  if ((int)uVar1 < 0x38) {
    if (uVar1 < 0x38) {
      return (&errno_str)[uVar1];
    }
                    /* WARNING: Does not return */
    pcVar2 = (code *)SoftwareBreakpoint(0x5512,0x106504);
    (*pcVar2)();
  }
  return "errno not found";
}

