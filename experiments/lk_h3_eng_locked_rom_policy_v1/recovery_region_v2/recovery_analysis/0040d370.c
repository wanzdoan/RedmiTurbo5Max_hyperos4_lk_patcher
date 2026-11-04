
/* android::base::ShouldLog(android::base::LogSeverity, char const*) */

bool android::base::ShouldLog(uint param_1,undefined8 param_2)

{
  bool bVar1;
  int iVar2;

  iVar2 = __isOSVersionAtLeast(0x1e,0,0);
  if (iVar2 == 0) {
    bVar1 = DAT_0042ccf8 <= (int)param_1;
  }
  else {
    iVar2 = param_1 + 2;
    if (4 < param_1) {
      iVar2 = 7;
    }
    iVar2 = __android_log_is_loggable(iVar2,param_2,4);
    bVar1 = iVar2 != 0;
  }
  return bVar1;
}
